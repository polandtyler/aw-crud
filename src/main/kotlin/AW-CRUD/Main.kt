package `AW-CRUD`
import io.javalin.Javalin
import java.lang.Exception
import io.javalin.apibuilder.ApiBuilder.*

fun main(args: Array<String>) {
    val userDao = UserDao()

    val app = Javalin.create().apply {
        exception(Exception::class.java) {e, ctx -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("not found") }
    }.start(7000)

    app.routes {

        get("/users") { ctx ->
            ctx.json(userDao.users)
        }

        get("/users/:user-id") {
            it.json(userDao.findById(it.pathParam("user-id").toInt()) ?: 0) // will never be 0 b/c we know its there
        }

        get("users/email/:email") {
            it.json(userDao.findByEmail(it.pathParam("email")) ?: "")
        }

        post("/users") {
            val user = it.body<User>()
            userDao.save(name = user.name, email = user.email)
            it.status(201)
        }

        patch("/users/:user-id") {
            val user = it.body<User>()
            userDao.update(
                id = it.pathParam("user-id").toInt(),
                user = user
            )
            it.status(204)
        }

        delete("/users/:user-id") {
            userDao.delete(it.pathParam("user-id").toInt())
            it.status(204)
        }
    }
}

