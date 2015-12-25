package controllers

import com.google.inject.Inject
import models.UserDAO
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, Controller}

/**
  * Created by sabonis on 12/25/15.
  */
class Users @Inject()(userDAO: UserDAO) extends Controller {

  def getAll = Action.async {
    userDAO.all().map(Ok apply _.toString())
  }

}
