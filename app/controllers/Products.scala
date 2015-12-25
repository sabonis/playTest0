package controllers

import play.api.mvc._
import play.api.i18n._
import play.api.data.Form
import play.api.data.Forms.{mapping, longNumber, nonEmptyText}
import models.Product

class Products @javax.inject.Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  private val productForm: Form[Product] = Form(
    mapping(
      "ean" → longNumber.verifying(
        "validation.ean.duplicate", Product.findbyEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )

  def list = Action { implicit request =>
    val products = Product.findAll

    Ok(views.html.products.list(products))
  }

  def show(ean: Long) = Action { implicit request =>
    Product.findbyEan(ean).map { product => 
      Ok(views.html.products.details(product))
    }.getOrElse(NotFound)
  }

  def save = Action { request =>
    val newProductForm = productForm.bindFromRequest()(request)

    newProductForm.fold(
      hasErrors = form => Redirect(routes.Products.newProduct())
        .flashing(Flash(form.data + ("error" -> Messages("validation.errors")))),
      success = { newProduct =>
        Product.add(newProduct)
        val message = Messages("products.new.success", newProduct.name)
        Redirect(routes.Products.show(newProduct.ean)).
          flashing("success" -> message)
      }
    )
  }

  def newProduct = Action { implicit request =>
      val form = if (request.flash.get("error").isDefined)
        productForm.bind(request.flash.data)
      else productForm
      Ok(views.html.products.editProduct(form))
    }
}
