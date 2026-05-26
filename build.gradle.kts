
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.hophey"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {

    // Core (contentNegotiation, JSON, logs, engine etc)
    implementation(ktorLibs.client.apache)
    implementation(ktorLibs.client.core)
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.server.netty)
    implementation("io.ktor:ktor-server-cors:3.4.0")
    implementation("io.ktor:ktor-server-status-pages:3.4.0")

    // /health endpoint
    implementation(libs.hayden.khealth)

    // DI
    implementation(libs.koin.ktor)

    // Logs
    implementation(libs.koin.loggerSlf4j)
    implementation(libs.logback.classic)

    // Exposed/DB
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("org.jetbrains.exposed:exposed-core:1.3.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.3.0")
    implementation("org.jetbrains.exposed:exposed-dao:1.3.0")
    implementation(libs.postgresql)
    implementation(libs.h2database.h2)


    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
