package models.json

case class WithdrawResponse(jsonrpc: String, result: Withdraw)
case class Withdraw(address: String, amount: BigDecimal, confirmed_timestamp: Option[Long], created_timestamp: Long, currency: String, fee: BigDecimal, priority: Int, state: String, transaction_id: Option[String], updated_timestamp: Option[Long])
case class WithdrawStatus(success: Boolean)
