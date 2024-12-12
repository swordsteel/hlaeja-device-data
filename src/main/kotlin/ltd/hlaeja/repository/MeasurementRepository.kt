package ltd.hlaeja.repository

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.write.Point
import com.influxdb.query.FluxTable
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ltd.hlaeja.properties.InfluxDbProperties
import org.springframework.stereotype.Repository

@Repository
class MeasurementRepository(
    private val influxDBClient: InfluxDBClient,
    private val properties: InfluxDbProperties,
) {

    private val writeApi = influxDBClient.makeWriteApi()

    companion object {
        const val BY_NODE_QUERY: String = """
          from(bucket: "%s")
            |> range(start: -1y)
            |> filter(fn: (r) => r["_measurement"] == "%s")
            |> filter(fn: (r) => r["node"] == "%s")
            |> group(columns: ["_field"])
            |> sort(columns: ["_time"], desc: true)
            |> limit(n: 1)
        """
    }

    suspend fun save(
        point: Point,
    ) = withContext(Dispatchers.IO) { writeApi.writePoint(point) }

    suspend fun getByNode(
        client: UUID,
        device: UUID,
    ): MutableList<FluxTable> = influxDBClient.queryApi
        .query(
            BY_NODE_QUERY.format(properties.bucket, client, device).trimIndent(),
            properties.org,
        )
}
