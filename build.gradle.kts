import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import java.lang.System.getenv

plugins {
    alias(hlaeja.plugins.com.bmuschko.docker)
    alias(hlaeja.plugins.kotlin.jvm)
    alias(hlaeja.plugins.kotlin.spring)
    alias(hlaeja.plugins.ltd.hlaeja.plugin.service)
    alias(hlaeja.plugins.spring.dependency.management)
    alias(hlaeja.plugins.springframework.boot)
}

dependencies {
    implementation(hlaeja.influxdb.client.kotlin)
    implementation(hlaeja.kotlin.logging)
    implementation(hlaeja.kotlin.reflect)
    implementation(hlaeja.kotlinx.coroutines)
    implementation(hlaeja.library.hlaeja.common.messages)
    implementation(hlaeja.springboot.starter.actuator)
    implementation(hlaeja.springboot.starter.webflux)

    testImplementation(hlaeja.mockk)
    testImplementation(hlaeja.projectreactor.reactor.test)
    testImplementation(hlaeja.kotlin.test.junit5)
    testImplementation(hlaeja.kotlinx.coroutines.test)
    testImplementation(hlaeja.springboot.starter.test)

    testRuntimeOnly(hlaeja.junit.platform.launcher)
}

group = "ltd.hlaeja"

fun influxDbToken(): String = if (extra.has("influxdb.token")) {
    extra["influxdb.token"] as String
} else {
    getenv("INFLUXDB_TOKEN") ?: "missing_token"
}

tasks {
    named("containerCreate", DockerCreateContainer::class) {
        withEnvVar("INFLUXDB_TOKEN", influxDbToken())
    }
    withType<ProcessResources> {
        filesMatching("**/application.yml") { filter { it.replace("%INFLUXDB_TOKEN%", influxDbToken()) } }
        onlyIf { file("src/main/resources/application.yml").exists() }
    }
}
