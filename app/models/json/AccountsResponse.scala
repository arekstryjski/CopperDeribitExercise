package models.json

case class AccountsResponse(jsonrpc: String, result: Array[Account])
case class Account(username: String, `type`: String, portfolio: Map[String, Balances])
case class Balances(currency: String, balance: BigDecimal, available_withdrawal_funds: BigDecimal, available_funds: BigDecimal)

