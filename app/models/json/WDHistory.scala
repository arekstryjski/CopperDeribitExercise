package models.json

import play.api.libs.json.JsArray

case class WDHistory(withdrawals: JsArray, deposits: JsArray)

case class WithdrawalsResponse(jsonrpc: String, result: Withdrawals)
case class Withdrawals(count: Int, data: JsArray)

case class DepositsResponse(jsonrpc: String, result: Deposits)
case class Deposits(count: Int, data: JsArray)
