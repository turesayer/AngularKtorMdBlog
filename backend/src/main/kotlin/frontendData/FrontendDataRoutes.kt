package koeln.sayer.frontendData

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.frontendDataRoutes() {
    get("posts") {
        call.respond(
            HttpStatusCode.OK,
            listOf(
                mapOf(
                    "title" to "A Simple Blog Engine",
                    "path" to "/content/2025-06-15-base-blog-engine.md"
                ),
                mapOf(
                    "title" to "Hello World",
                    "path" to "/content/2025-06-14-hello-world.md"
                )
            )
        )
    }
}