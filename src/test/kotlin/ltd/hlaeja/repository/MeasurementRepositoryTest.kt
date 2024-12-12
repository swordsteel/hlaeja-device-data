package ltd.hlaeja.repository

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.QueryApi
import com.influxdb.client.WriteApi
import com.influxdb.client.write.Point
import com.influxdb.query.FluxTable
import io.mockk.Runs
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.properties.InfluxDbProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach

class MeasurementRepositoryTest {
    private val client: InfluxDBClient = mockk()
    private val properties: InfluxDbProperties = mockk()
    private val writeApi: WriteApi = mockk()
    private val queryApi: QueryApi = mockk()
    private lateinit var repository: MeasurementRepository

    @BeforeEach
    fun setUp() {
        every { client.makeWriteApi() } returns writeApi
        repository = MeasurementRepository(client, properties)
    }

    @Test
    fun `save point to influxdb`() = runTest {
        // given
        val point = Point.measurement("test").addField("value", 12.3)
        every { writeApi.writePoint(any()) } just Runs

        // when
        repository.save(point)

        // then
        coVerify { writeApi.writePoint(point) }
    }

    @Test
    fun `load fields from influxdb by client and device`() = runTest {
        // given
        val clientUuid = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val deviceUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val mockResult = mutableListOf(FluxTable())

        every { client.queryApi } returns queryApi
        properties.apply {
            every { bucket } returns "bucket"
            every { org } returns "organization"
        }
        every { queryApi.query(any(String::class), any(String::class)) } returns mockResult

        // when
        val result = repository.getByNode(clientUuid, deviceUuid)

        // then
        assertEquals(mockResult, result)
        verify {
            queryApi.query(
                "from(bucket: \"bucket\")\n" +
                    "  |> range(start: -1y)\n" +
                    "  |> filter(fn: (r) => r[\"_measurement\"] == \"00000000-0000-0000-0000-000000000000\")\n" +
                    "  |> filter(fn: (r) => r[\"node\"] == \"00000000-0000-0000-0000-000000000001\")\n" +
                    "  |> group(columns: [\"_field\"])\n" +
                    "  |> sort(columns: [\"_time\"], desc: true)\n" +
                    "  |> limit(n: 1)",
                "organization",
            )
        }
    }
}
