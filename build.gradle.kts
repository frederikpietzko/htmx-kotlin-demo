plugins {
    kotlin("jvm") version "2.2.20" apply false
    kotlin("plugin.spring") version "2.2.20" apply false
    kotlin("multiplatform") version "2.2.20" apply false
    id("io.ktor.plugin") version "3.3.2" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.20" apply false
}

subprojects {
    group = "com.github.frederikpietzko"
    version = "0.0.1"

    repositories {
       mavenCentral()
    }
}
