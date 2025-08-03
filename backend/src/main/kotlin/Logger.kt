package koeln.sayer

import io.ktor.util.logging.KtorSimpleLogger

fun Any.logger() =
    KtorSimpleLogger(this::class.simpleName ?: "Unable to receive class name")