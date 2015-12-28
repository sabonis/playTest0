package models

import com.google.inject.{Singleton, ImplementedBy}
import play.api.libs.json.Json
import slick.dbio.DBIOAction

import scala.concurrent.Future

/**
  * Created by sabonis on 12/25/15.
  */
object User extends {
  implicit val userReads = Json.reads[User]
}

case class User(
            id: Option[Int] = None,
            facebookId: Int,
            username: String,
            gender: Int
          ) {

  /*
  id:
  type: DataType.INTEGER
  primaryKey: true
  autoIncrement: true

  facebookId:
  type: DataType.STRING
  defaultValue: null

  facebookMeta:
  type: DataType.JSON
  defaultValue: null

  username:
  type: DataType.STRING
  unique: true
  allowNull: false,
  validate:
    is: /^\ w[\ w \.\-] * $ /,
  len:[6, 32],

  email:
  type: DataType.STRING
  unique: true
  allowNull: false
  validate:
    isEmail: true

  isEmailVerified:
  type: DataType.BOOLEAN
  defaultValue: false

  emailVerificationCode:
  type: DataType.STRING

  mobile:
  type: DataType.STRING
  unique: true
  validate:
    is: /^\+\ d +$ /
    len:[3, 16]

  isMobileVerified:
  type: DataType.BOOLEAN
  defaultValue: false

  mobileVerificationCode:
  type: DataType.STRING

  passwordHash:
  type: DataType.STRING
  allowNull: false

  refreshToken:
  type: DataType.STRING
  set: ( val) ->
  @setDataValue 'refreshToken ',
  val
  date = unless
  val then null else (moment().add Config.security.refreshToken.validity).toDate()
  @setDataValue 'refreshTokenExpirationDate ', date

  refreshTokenExpirationDate:
  type: DataType.DATE

  type:
  type: DataType.STRING
  allowNull: false
  defaultValue: 'shopowner '
  validate: appValidator.isSupportedUserType.db_validator

  passwordResetToken:
  type: DataType.STRING
  set: ( val) ->
  @setDataValue 'passwordResetToken ',
  val
  date = unless
  val then null else (moment().add Config.security.passwordResetToken.validity).toDate()
  @setDataValue 'passwordResetTokenExpirationDate ', date

  passwordResetTokenExpirationDate:
  type: DataType.DATE

  platformServiceFeePercentage:
  type: DataType.FLOAT
  defaultValue: 0

  bankName:
  type: DataType.TEXT

  bankAccount:
  type: DataType.TEXT#quotas / state

  state:
  type: DataType.STRING
  defaultValue: 'ACTIVE '
  validate: appValidator.isSupportedObjectState.db_validator

  childrenQuota:
  type: DataType.INTEGER
  defaultValue: 0
  validate:
    min: 0

  descendantQuota:
  type: DataType.INTEGER
  defaultValue: 0
  validate:
    min: 0#preference

  preferredCurrency:
  type: DataType.STRING(3)
  defaultValue: 'TWD '#XXX(YC) default to show in TWD for development purpose
    validate: appValidator.isSupportedCurrency.db_validator

  preferredLanguage:
  type: DataType.STRING
  defaultValue: 'zh_Hant '#XXX(YC) default to show in traditional chinse for development purpose
    validate: appValidator.isSupportedLanguage.db_validator

  preferredTimezone:
  type: DataType.STRING
  defaultValue: 'Asia / Taipei '#XXX(YC) default to be in Taipei for development purpose
    validate: appValidator.isSupportedTimezone.db_validator

  enablePushNotification:
  type: DataType.BOOLEAN
  defaultValue: true#personal info

  displayName:
  type: DataType.STRING
  allowNull: true
  defaultValue: null
  validate:
    is: /^[\ w \ s] * $ /,

  dateOfBirth:
  type: DataType.DATE
  allowNull: true
  defaultValue: null

  gender:
  type: DataType.STRING(1)
  defaulValue: null
  validate:
    isIn: [['M', 'F']]

*/
}

import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

@ImplementedBy(classOf[UserDAOImpl0])
trait UserDAO {

  def all: Future[List[User]]
  def insert(cat: User): Future[Unit]

}

@Singleton
class UserDAOImpl0 extends HasDatabaseConfig[JdbcProfile] with UserDAO {
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  private val Users = TableQuery[UsersTable]

  val setup = DBIOAction.seq(
    Users.schema.create,
    Users += User(None, 1, "admin", 3),
    Users += User(None, 1, "kk", 2),
    Users += User(None, 1, "jj", 2),
    Users += User(None, 1, "ii", 3)
  )
  db.run(setup)

  override def all: Future[List[User]] = db.run(Users.result).map(_.toList)

  override def insert(user: User): Future[Unit] = {
    //val userWithIdNone = user.copy(id = None)
    db.run(Users += user).map(_ => ())
  }

  def withSexTwo: Future[List[User]] = db.run(
    Users.filter(_.gender === 2).result
  ).map(_.toList)

  private class UsersTable(tag: Tag) extends Table[User](tag, "User") {

    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def facebookId = column[Int]("FACEBOOKID")
    def username = column[String]("USERNAME")
    def gender = column[Int]("GENDER")

    def * = (id.?, facebookId, username, gender) <> ((User.apply _).tupled, User.unapply)
  }

}
