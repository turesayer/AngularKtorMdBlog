package koeln.sayer

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        singlePageApplication {
            useResources = true
            angular("frontend/browser")
        }
    }
}
