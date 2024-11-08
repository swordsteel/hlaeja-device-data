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
    implementation(hlaeja.com.influxdb.client.kotlin)
    implementation(hlaeja.kotlin.reflect)
    implementation(hlaeja.kotlinx.coroutines)
    implementation(hlaeja.org.springframework.springboot.actuator.starter)
    implementation(hlaeja.org.springframework.springboot.webflux.starter)

    testImplementation(hlaeja.io.mockk)
    testImplementation(hlaeja.io.projectreactor.reactor.test)
    testImplementation(hlaeja.kotlin.test.junit5)
    testImplementation(hlaeja.kotlinx.coroutines.test)
    testImplementation(hlaeja.org.springframework.springboot.test.starter)

    testRuntimeOnly(hlaeja.org.junit.platform.launcher)
}

group = "ltd.hlaeja"

tasks {
}

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
