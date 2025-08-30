import io.ktor.plugin.features.*

val koin_version: String by project
val kotlin_version: String by project
val kotlinx_datetime_version: String by project
val kotlinx_serialization_version: String by project
val ktor_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.2.10"
    id("io.ktor.plugin") version "3.1.3"
    kotlin("plugin.serialization") version "2.2.10"
}

kotlin {
    jvmToolchain(21)
}

group = "koeln.sayer"
version = "0.0.4"

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
        jib {
            dockerClient {
                // This is necessary in case anyone not using docker but podman or some other tool
                // needs to provide a path to their "docker" executable. In case of podman on mac this
                // env var should be set to /opt/podman/bin/podman
                providers.environmentVariable("DOCKER_EXECUTABLE").orNull?.let {
                    executable = it
                }
                project.logger.lifecycle("Using docker executable: $executable")
            }
            providers.environmentVariable("MD_FILE_LOCATION").orNull?.let {
                extraDirectories {
                    paths {
                        path {
                            setFrom(it)
                            into = "/posts"
                        }
                    }
                }
                project.logger.lifecycle("Using extra directory: $it")
            }
        }
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

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_serialization_version")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinx_datetime_version")

    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
