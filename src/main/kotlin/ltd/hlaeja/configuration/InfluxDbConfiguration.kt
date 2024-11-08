package ltd.hlaeja.configuration

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.InfluxDBClientFactory
import ltd.hlaeja.properties.InfluxDbProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(InfluxDbProperties::class)
class InfluxDbConfiguration(
    private val properties: InfluxDbProperties,
) {

    @Bean
    fun influxDBClient(): InfluxDBClient = InfluxDBClientFactory.create(
        properties.url,
        properties.token.toCharArray(),
        properties.org,
        properties.bucket,
    )
}
