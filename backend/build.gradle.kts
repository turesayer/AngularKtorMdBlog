import io.ktor.plugin.features.*

val koin_version: String by project
val kotlin_version: String by project
val ktor_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.1.21"
    id("io.ktor.plugin") version "3.1.3"
}

kotlin {
    jvmToolchain(21)
}

group = "koeln.sayer"
version = "0.0.2"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

ktor {
    docker {
        localImageName.set("blogging_engine")
        imageTag.set(project.version.toString())
        externalRegistry.set(
            DockerImageRegistry.externalRegistry(
                username = providers.environmentVariable("DOCKER_USER"),
                password = providers.environmentVariable("DOCKER_PW"),
                project = providers.environmentVariable("DOCKER_PROJECT"),
                hostname = providers.environmentVariable("DOCKER_HOST"),
            )
        )
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("io.ktor:ktor-server-content-negotiation:${ktor_version}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")

    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
