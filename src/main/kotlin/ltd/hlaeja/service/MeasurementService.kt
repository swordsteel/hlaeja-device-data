package ltd.hlaeja.service

import com.influxdb.client.write.Point
import java.util.UUID
import ltd.hlaeja.library.deviceData.MeasurementData
import ltd.hlaeja.repository.MeasurementRepository
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class MeasurementService(
    private val repository: MeasurementRepository,
) {

    suspend fun getNodeMeasurement(
        client: UUID,
        node: UUID,
    ): MeasurementData.Response {
        val result = repository.getByNode(client, node)
        if (result.isEmpty()) {
            throw ResponseStatusException(NOT_FOUND, "No data for client: $client, device: $node")
        }
        val latestData = mutableMapOf<String, Number>()
        result.forEach { table ->
            table.records.forEach { record ->
                latestData[record.getValueByKey("_field") as String] = record.value as Number
            }
        }
        return MeasurementData.Response(latestData)
    }

    suspend fun addMeasurement(
        client: UUID,
        request: MeasurementData.Request,
    ) = Point.measurement(client.toString())
        .also { point ->
            addTags(request.tags, point)
            addFields(request.fields, point)
        }
        .let { point -> repository.save(point) }

    private suspend fun addFields(
        measurements: Map<String, Number>,
        point: Point,
    ) = measurements.forEach { (key, value) -> point.addField(key, value) }

    private suspend fun addTags(
        measurements: Map<String, String>,
        point: Point,
    ) = measurements.forEach { (key, value) -> point.addTag(key, value) }
}
