package controllers

import play.api.Play
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

import scala.concurrent.Future

class Application extends Controller {

  def index = Action {
    //Ok(views.html.index("Your new application is ready."))
    Ok("Fuck world.")
    //Redirect(routes.Products.list())
  }

  def hello = Action {
    Ok("Fuck world.")
  }

  def test = Action {
    Ok(Play.current.configuration.getString("environment.user").get)
  }

  def testFuture = Action.async {
    val someShitNeedsWait = Future("shit")
    someShitNeedsWait map (Ok(_))
  }

}
