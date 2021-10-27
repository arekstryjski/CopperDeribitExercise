package models.db

import anorm._
import models.json.{SimpleBalance, SimpleBalances}
import play.api.db.Database

import scala.concurrent.Future

trait DBAccess {
  val db: Database
  val dbCtx: DatabaseExecutionContext

  val parser: RowParser[SimpleBalance] = Macro.namedParser[SimpleBalance]

  val selectQ = SQL"select * from SimpleBalances"
  def insertQ(b: SimpleBalance) =
    SQL"insert into SimpleBalances (currency, current, reserved) values (${b.currency}, ${b.current}, ${b.reserved})"
  def updateQ(b: SimpleBalance) =
    SQL"update SimpleBalances set current = ${b.current}, reserved = ${b.reserved} where currency = ${b.currency}"

  def updateSimpleBalances(bs: SimpleBalances): Unit = {
    Future {
      db.withConnection { implicit c =>
        val old = selectQ.as(parser.*)
        println(s"[BD]: select: $old")

        val oldMap = old.foldLeft(Map[String,SimpleBalance]())((map, b) => map + (b.currency -> b))

        bs.balances.foreach((b: SimpleBalance) => {
          val maybeOld = oldMap.get(b.currency)
          maybeOld match {
            case Some(SimpleBalance(_, oc, or)) if oc != b.current || or != b.reserved => {
              println(s"[BD]: update $b")
              updateQ(b).executeUpdate()
            }
            case None => {
              println(s"[BD]: insert $b")
              insertQ(b).executeInsert()
            }
            case _ => ()
          }
        })
      }
    }(dbCtx)
  }

}
