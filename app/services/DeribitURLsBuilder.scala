package services

object DeribitURLsBuilder {
  private val id = "ndo4HHXM"
  private val secret = "J5UtE25BjXiq6mRQyzRnw_hAS2XtvIAEK21Z3tNQ7Fw"

  private val domain = "https://test.deribit.com"
  private val api = "api/v2"

  def authUrl = s"$domain/$api/public/auth?client_id=$id&client_secret=$secret&grant_type=client_credentials"

  def subAccountsUrl = s"$domain/$api/private/get_subaccounts?with_portfolio=true"
  def accountSummaryUrl = s"$domain/$api/private/get_account_summary?currency=BTC&extended=true"

  def depositsUrl(count: Int, offset: Int = 0, currency: String = "BTC") =
    s"$domain/$api/private/get_deposits?currency=$currency&count=$count&offset=$offset"

  def withdrawalsUrl(count: Int, offset: Int = 0, currency: String = "BTC") =
    s"$domain/$api/private/get_withdrawals?currency=$currency&count=$count&offset=$offset"

  def subTransferUrl(amount: BigDecimal, currency: String, destination: Int) =
    s"$domain/$api/private/submit_transfer_to_subaccount?amount=$amount&currency=$currency&destination=$destination"

  def makeWithdrawalsUrl(amount: BigDecimal, currency: String, address: String) =
    s"$domain/$api/private/withdraw?currency=$currency&address=$address&amount=$amount"
}
