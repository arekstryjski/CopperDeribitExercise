package models.json

//case class SubTransferResponse(jsonrpc: String, result: Option[SubTransfer], error: Option[Error])
case class SubTransferResponse(jsonrpc: String, result: SubTransfer)
case class SubTransfer(updated_timestamp: Long, `type`: String, state: String, source: Int, other_side: String, direction: String, created_timestamp: Long, amount: BigDecimal)
case class SubTransferStatus(success: Boolean)
