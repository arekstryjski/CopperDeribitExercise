package controllers

import models.db.{DBAccess, DatabaseExecutionContext}
import models.json
import models.json.{AuthResult, Err, SimpleBalances, SubTransferStatus, WDHistory, WithdrawStatus}
import play.api.db.Database
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents}
import services.DeribitCom

import javax.inject._
import scala.concurrent.ExecutionContext
import models.json.JsonMapper._


@Singleton
class DeribitController @Inject() (cc: ControllerComponents, val ws: WSClient, val db: Database, val dbCtx: DatabaseExecutionContext)
                                  (implicit exec: ExecutionContext)
  extends AbstractController(cc) with DeribitCom with DBAccess {

  private def mapResponse[V](toJson: V => JsValue)(e: Either[Err, V]) = {
    e match {
      case Right(v) =>  Ok(toJson(v))
      case Left(e) => InternalServerError(Json.toJson(e))
    }
  }
  private def mapResponseWithSE[V](toJson: V => JsValue, sideEffect: V => Unit)(e: Either[Err, V]) = {
    e match {
      case Right(v) => {
        sideEffect(v)
        Ok(toJson(v))
      }
      case Left(e) => InternalServerError(Json.toJson(e))
    }
  }

  def index = Action.async {
      authenticate.map((r: AuthResult) => Ok(Json.toJson(r)))
  }

  def subAccounts = Action.async {
    getSubAccounts.map(r => Ok(r))
  }

  def balances = Action.async {
    getBalances
      .map(mapResponseWithSE[SimpleBalances](Json.toJson(_), updateSimpleBalances))
  }


  def history = Action.async {
    val withdrawals = getWithdrawals
    val deposits = getDeposits

    val hist = for {
      w <- withdrawals
      d <- deposits
    } yield WDHistory(w.data, d.data)

    hist.map(h => Ok(Json.toJson(h)))
    // TODO: better error handling should be also implemented here as in the other functions
  }

  def transfer(amount: Double, currency: String, destination: Int) = Action.async {
    getSubTransfer(amount, currency, destination)
      .map(mapResponse[SubTransferStatus](Json.toJson(_)))
  }

  def withdraw(amount: Double, currency: String, address: String) = Action.async {
    getWithdraw(amount, currency, address)
      .map(mapResponse[WithdrawStatus](Json.toJson(_)))
  }

}
