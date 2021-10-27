package services

import models.json.{Account, AuthResult, Deposits, Err, SimpleBalance, SimpleBalances, SubTransfer, SubTransferStatus, Withdraw, WithdrawStatus, Withdrawals}
import play.api.libs.json.JsValue
import play.api.libs.ws.{WSClient, WSResponse}
import services.DeribitURLsBuilder.{accountSummaryUrl, authUrl, depositsUrl, makeWithdrawalsUrl, subAccountsUrl, subTransferUrl, withdrawalsUrl}
import models.json.JsonMapper.{accountsResult, authResult, depositsResult, subTransferResult, withdrawResult, withdrawalsResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future



trait DeribitCom  {
  val ws: WSClient

  // TODO: There should be also the way to refresh it
  var token: Option[String] = None

  def authenticate: Future[AuthResult] = {
      ws.url(authUrl)
        .addHttpHeaders("Accept" -> "application/json")
        .get()
        .flatMap((res: WSResponse) => {
          val eventualAuthResult = authResult(res.json)
          eventualAuthResult.foreach((result: AuthResult) => token = Some(result.access_token))
          eventualAuthResult
        })
  }

  def get(url: String, t: String): Future[WSResponse] = {
    ws.url(url)
      .addHttpHeaders(
        ("Authorization" -> (s"Bearer $t")),
        ("Accept" -> "application/json")
      )
      .get()
  }

  def authGet(url: String): Future[WSResponse] = {
    token match {
      case None => authenticate.flatMap((aResult: AuthResult) => get(url, aResult.access_token))
      case Some(t) => get(url, t)
    }
  }

  def getSubAccounts: Future[JsValue] = {
    authGet(subAccountsUrl).map((r: WSResponse) => r.json)
  }

  def getWithdrawals: Future[Withdrawals] = {
    authGet(withdrawalsUrl(100))
      .flatMap((r: WSResponse) => withdrawalsResult(r.json))
  }

  def getDeposits: Future[Deposits] = {
    authGet(depositsUrl(100))
      .flatMap((r: WSResponse) => depositsResult(r.json))
  }

  def getBalances: Future[Either[Err, SimpleBalances]] = {
    authGet(subAccountsUrl)
      .flatMap((r: WSResponse) => {
        accountsResult(r.json)
          .map((e: Either[Err, Seq[Account]]) => {
            e match {
              case Left(error) => Left(error)
              case Right(accounts) =>
                val sb = accounts
                  .flatMap((a: Account) => a.portfolio.values)
                  .map(b => SimpleBalance(b.currency, b.balance, b.balance - b.available_withdrawal_funds))
                  .filterNot(b => b.current == 0 && b.reserved == 0)
                  .foldLeft(SimpleBalances())((acc: SimpleBalances, b: SimpleBalance) => acc + b)
                Right(sb)
            }
          })
      })
  }

  def getSubTransfer(amount: BigDecimal, currency: String, destination: Int): Future[Either[Err, SubTransferStatus]] = {
    authGet(subTransferUrl(amount, currency, destination))
      .flatMap((r: WSResponse) => {
        subTransferResult(r.json)
          .map((e: Either[Err, SubTransfer]) => {
            e match {
              case Left(error) => Left(error)
              case Right(t) => Right(SubTransferStatus(true)) // TODO: maybe something should be taken from response?
            }
          })
      })
  }

  def getWithdraw(amount: BigDecimal, currency: String, address: String): Future[Either[Err, WithdrawStatus]] = {
    authGet(makeWithdrawalsUrl(amount, currency, address))
      .flatMap((r: WSResponse) => {
        withdrawResult(r.json)
          .map((e: Either[Err, Withdraw]) => {
            e match {
              case Left(error) => Left(error)
              case Right(t) => Right(WithdrawStatus(true)) // TODO: maybe something should be taken from response?
            }
          })
      })
  }

}
