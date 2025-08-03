package koeln.sayer.frontendData

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.dsl.module

val FrontendDataModule = module {
    single { FrontendDataService(getProperty("posts.file_location")) }
}

fun Application.frontendDataModule() {
    routing {
        route("api/frontenddata") {
            frontendDataRoutes()
        }
    }
}