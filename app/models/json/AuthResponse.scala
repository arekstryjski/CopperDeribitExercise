package models.json

case class AuthResponse(jsonrpc: String, result: AuthResult)
case class AuthResult(access_token: String, expires_in: Int, refresh_token: String, scope: String, token_type: String)
