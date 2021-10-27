package models.json

case class SimpleBalances(balances: List[SimpleBalance] = Nil) {

  def +(b: SimpleBalance): SimpleBalances = {
    val l =
      if (balances == Nil) List(b)
      else if (balances.filter(_.currency == b.currency) != Nil) {
        balances.foldLeft(List(): List[SimpleBalance])((acc: List[SimpleBalance], v: SimpleBalance) =>
          if (b.currency == v.currency) SimpleBalance(b.currency, b.current + v.current, b.reserved + v.reserved) :: acc
          else v :: acc
        )
      }
      else b :: balances
    SimpleBalances(l)
  }

}

case class SimpleBalance(currency: String, current: BigDecimal = 0, reserved: BigDecimal = 0)
