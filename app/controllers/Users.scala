package controllers

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import models.{User, UserDAOImpl0}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

/**
  * Created by sabonis on 12/25/15.
  */
class Users extends Controller {

  val userDAO = new UserDAOImpl0

  def getAll = Action.async {
    userDAO.all.map(Ok apply _.toString())
  }

  def insert = Action.async(parse.tolerantJson) { req =>
    //val user = new Gson().fromJson(req.body.toString(), classOf[User])
    userDAO.insert(req.body.validate[User].get).map(Ok apply _.toString)
    //Future(Ok(req.body.toString()))
  }

  def getSexTwo = Action(parse.json) { request =>
    (request.body \ "test").asOpt[Int].map { name => Ok(name.toString) }.getOrElse(BadRequest)
    //userDAO.withSexTwo.map(Ok apply _.toString())
  }

}
