package koeln.sayer

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureFrontend() {
    routing {
        singlePageApplication {
            useResources = true
            angular("frontend/browser")
        }
    }
}
