package models.json

import play.api.libs.json.JsObject

case class ErrorResponse(jsonrpc: String, error: Err)
case class Err(message: String, data: Option[JsObject], code: Option[Int])
