package ltd.hlaeja.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "influxdb")
data class InfluxDbProperties(
    val url: String,
    val token: String,
    val org: String,
    val bucket: String,
)
