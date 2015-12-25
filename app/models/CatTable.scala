package models

import dao.Cat
import slick.driver.JdbcProfile

/**
  * Created by sabonis on 12/24/15.
  */
trait CatTable {
  protected val driver: JdbcProfile

  import driver.api._

  class Cats(tag: Tag) extends Table[Cat](tag, "CATS") {
    def id = column[Int]("ID", O.PrimaryKey)

    // This is the primary key column
    def name = column[String]("NAME")

    def color = column[String]("COLOR")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, name, color) <>(Cat.tupled, Cat.unapply)
  }

}
