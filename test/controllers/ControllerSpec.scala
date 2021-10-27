package controllers

import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc._
import play.api.test._

class ControllerSpec extends PlaySpecification with Results {

  "respond to / return access_token" in new WithApplication(GuiceApplicationBuilder().build()) {
    val Some(result) = route(app, FakeRequest(GET, "/"))

    status(result) must equalTo(OK)
    contentType(result) must beSome("application/json")
    contentAsString(result) must contain("access_token")
  }

  "respond to /balances" in new WithApplication(GuiceApplicationBuilder().build()) {
    val Some(result) = route(app, FakeRequest(GET, "/balances"))

    status(result) must equalTo(OK)
    contentType(result) must beSome("application/json")
    contentAsString(result) must contain("balances")
  }

  "respond to /history" in new WithApplication(GuiceApplicationBuilder().build()) {
    val Some(result) = route(app, FakeRequest(GET, "/history"))

    status(result) must equalTo(OK)
    contentType(result) must beSome("application/json")
    contentAsString(result) must contain("""{"updated_timestamp":1635176024310,"transaction_id":"0d413eb53251e8696eb7e8bea5b15a6cae3e89657468ae1c62594286fc84b44f","state":"completed","received_timestamp":1635175989202,"note":"","currency":"BTC","amount":0.00001,"address":"2N8jf6pTNnHQHZ7Xo3Y8YnHDHPcsnzJf8EJ"}""")
  }

  "respond to /transfer" in new WithApplication(GuiceApplicationBuilder().build()) {
    val Some(result) = route(app, FakeRequest(GET, "/transfer?amount=0.000001&currency=BTC&destination=34624"))

    status(result) must equalTo(OK)
    contentType(result) must beSome("application/json")
    contentAsString(result) must contain("""{"success":true}""")
  }

  "respond to negative /transfer" in new WithApplication(GuiceApplicationBuilder().build()) {
    val Some(result) = route(app, FakeRequest(GET, "/transfer?amount=-0.000001&currency=BTC&destination=34624"))

    status(result) must equalTo(INTERNAL_SERVER_ERROR)
    contentType(result) must beSome("application/json")
    contentAsString(result) must contain("positive float required")
  }

  "respond to /withdraw" in new WithApplication(GuiceApplicationBuilder().build()) {
    val Some(result) = route(app, FakeRequest(GET, "/withdraw?amount=0.000005&currency=BTC&address=2NFcyFkJXPhaa5tokSKSwwwYu3HZL1Zyc8z"))

    status(result) must equalTo(OK)
    contentType(result) must beSome("application/json")
    contentAsString(result) must contain("""{"success":true}""")
  }

}