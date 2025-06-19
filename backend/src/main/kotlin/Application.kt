package koeln.sayer

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import koeln.sayer.frontendData.FrontendDataModule
import koeln.sayer.frontendData.frontendDataModule
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(CORS) {
        // TODO: Make configurable or set as dev setting
        allowHost("localhost:4200")
    }
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
    install(Koin) {
        slf4jLogger()
        modules(
            FrontendDataModule
        )
    }

    frontendDataModule()
}
