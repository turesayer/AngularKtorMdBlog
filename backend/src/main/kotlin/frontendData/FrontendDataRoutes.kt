package koeln.sayer.frontendData

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import koeln.sayer.logger
import org.koin.ktor.ext.inject

fun Route.frontendDataRoutes() {
    val frontendDataService by application.inject<FrontendDataService>()

    get("posts") {
        val files = frontendDataService.getAllPosts()
        files.forEach { logger().info("Found post: $it") }
        call.respond(
            HttpStatusCode.OK,
            files.map { mapOf("path" to it) }
        )
    }

    get("posts/{filename}") {
        call.respond(
            HttpStatusCode.OK,
            frontendDataService.getPostContent(call.parameters["filename"] ?: "")
        )
    }
}