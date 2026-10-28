plugins {
    kotlin("jvm") version "2.4.10"
}

group = "ie.setu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Source: https://mvnrepository.com/artifact/io.github.oshai/kotlin-logging-jvm
    implementation("io.github.oshai:kotlin-logging-jvm:8.0.4")
    // Source: https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:2.0.18")
    // For Streaming XML and JSON objects to files
    implementation("tools.jackson.core:jackson-databind:3.1.4")
    implementation("tools.jackson.module:jackson-module-kotlin:3.1.4")
    implementation("tools.jackson.dataformat:jackson-dataformat-xml:3.1.4")
}


kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}