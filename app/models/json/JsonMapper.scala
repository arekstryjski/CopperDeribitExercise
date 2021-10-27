package models.json

import play.api.libs.json._

import scala.concurrent.Future


object JsonMapper {
  implicit val errorFormat = Json.format[Err]
  implicit val errorResponseFormat = Json.format[ErrorResponse]

  implicit val authResultFormat = Json.format[AuthResult]
  implicit val authResponseFormat = Json.format[AuthResponse]

  implicit val withdrawalsResultFormat = Json.format[Withdrawals]
  implicit val withdrawalsResponseFormat = Json.format[WithdrawalsResponse]
  implicit val depositsResultFormat = Json.format[Deposits]
  implicit val depositsResponseFormat = Json.format[DepositsResponse]

  implicit val withDepHistoryFormat = Json.format[WDHistory]

  implicit val balancesFormat = Json.format[Balances]
  implicit val accountFormat = Json.format[Account]
  implicit val accountsResponseFormat = Json.format[AccountsResponse]

  implicit val simpleBalanceFormat = Json.format[SimpleBalance]
  implicit val simpleBalancesFormat = Json.format[SimpleBalances]

  implicit val subTransferFormat = Json.format[SubTransfer]
  implicit val subTransferResponseFormat = Json.format[SubTransferResponse]
  implicit val subTransferStatusFormat = Json.format[SubTransferStatus]

  implicit val withdrawFormat = Json.format[Withdraw]
  implicit val withdrawResponseFormat = Json.format[WithdrawResponse]
  implicit val withdrawStatusFormat = Json.format[WithdrawStatus]


  private def result[W, R](from: JsValue => JsResult[W], result: W => R)(json: JsValue): Future[R] = {
    from(json) match {
      case JsSuccess(resp, _) => Future.successful(result(resp))
      case e @ JsError(_) => {
        val jsonErr = JsError.toJson(e).toString()
        println(json)
        println(jsonErr)
        Future.failed(new Exception(s"parsing errors: ${jsonErr}"))
      }
    }
  }

  private def eitherResult[W, R](toRight: JsValue => JsResult[W], result: W => R)(json: JsValue): Future[Either[Err, R]] = {
    toRight(json) match {
      case JsSuccess(resp, _) => Future.successful(Right(result(resp)))
      case JsError(_) => {
        Json.fromJson[ErrorResponse](json) match {
          case JsSuccess(jsonErr, _) => Future.successful(Left(jsonErr.error))
          case JsError(_) => {
            Future.failed(new Exception(s"Unrecoverable parsing error: ${json}"))
          }
        }
      }
    }
  }

  def authResult(json: JsValue): Future[AuthResult] =
    result[AuthResponse, AuthResult](Json.fromJson[AuthResponse](_), _.result)(json)

  def withdrawalsResult(json: JsValue): Future[Withdrawals] =
    result[WithdrawalsResponse, Withdrawals](Json.fromJson[WithdrawalsResponse](_), _.result)(json)

  def depositsResult(json: JsValue): Future[Deposits] =
    result[DepositsResponse, Deposits](Json.fromJson[DepositsResponse](_), _.result)(json)

  def accountsResult(json: JsValue): Future[Either[Err,Seq[Account]]] =
    eitherResult[AccountsResponse, Seq[Account]](Json.fromJson[AccountsResponse](_), _.result)(json)

  def subTransferResult(json: JsValue): Future[Either[Err,SubTransfer]] =
    eitherResult[SubTransferResponse, SubTransfer](Json.fromJson[SubTransferResponse](_), _.result)(json)

  def withdrawResult(json: JsValue): Future[Either[Err,Withdraw]] =
    eitherResult[WithdrawResponse, Withdraw](Json.fromJson[WithdrawResponse](_), _.result)(json)

}

