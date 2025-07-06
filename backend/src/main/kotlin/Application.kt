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
        /***
         * Since the Angular app will be served by the Ktor server itself we don't need to allow
         * any hosts in prod. To work without any hacks locally you can enable cors for the
         * Angular dev server
         */
        if (this@module.environment.config.property("ktor.deployment.environment").getString() != "production") {
            val frontendLocation = this@module.environment.config.property("frontend.dev.location").getString()
            val frontendPort = this@module.environment.config.property("frontend.dev.port").getString()
            allowHost("$frontendLocation:$frontendPort")
        }
    }
    install(ContentNegotiation) {
        json()
    }
    install(Koin) {
        slf4jLogger()
        modules(
            FrontendDataModule
        )
    }

    configureFrontend()

    frontendDataModule()
}
