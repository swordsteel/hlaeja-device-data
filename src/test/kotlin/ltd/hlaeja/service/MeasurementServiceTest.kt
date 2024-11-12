package ltd.hlaeja.service

import com.influxdb.client.write.Point
import com.influxdb.query.FluxRecord
import com.influxdb.query.FluxTable
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.library.deviceData.MeasurementData
import ltd.hlaeja.repository.MeasurementRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.server.ResponseStatusException

class MeasurementServiceTest {

    private val repository: MeasurementRepository = mockk()
    val uuid: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")

    private lateinit var service: MeasurementService

    @BeforeEach
    fun setUp() {
        service = MeasurementService(repository)
    }

    @Test
    fun `get measurement from client and device - success`() = runTest {
        // given
        val fluxTable = mockk<FluxTable>()
        val fluxRecord = mockk<FluxRecord>()

        coEvery { repository.getByNode(any(), any()) } returns mutableListOf(fluxTable)
        coEvery { fluxTable.records } returns mutableListOf(fluxRecord)
        coEvery { fluxRecord.getValueByKey(any()) } returns "field"
        coEvery { fluxRecord.value } returns 1

        // when
        val result = service.getNodeMeasurement(uuid, uuid)

        // then
        assertEquals(1, result.fields.size)
        assertEquals("field", result.fields.keys.first())
        assertEquals(1, result.fields.values.first())
    }

    @Test
    fun `get measurement from client and device - fail no result`() = runTest {
        // given
        coEvery { repository.getByNode(any(), any()) } returns mutableListOf()

        // when exception
        val exception = assertFailsWith<ResponseStatusException>(
            block = { service.getNodeMeasurement(uuid, uuid) },
        )

        // then
        assertEquals(NOT_FOUND, exception.statusCode)
        assertEquals(
            "No data for client: 00000000-0000-0000-0000-000000000000, device: 00000000-0000-0000-0000-000000000000",
            exception.reason,
        )
    }

    @Test
    fun `add measurement`() = runTest {
        // given
        val uuid = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val request = MeasurementData.Request(mapOf("tag" to "value"), mapOf("field" to 10))

        val capturedPointSlot = slot<Point>()
        coEvery { repository.save(capture(capturedPointSlot)) } just Runs

        // when
        service.addMeasurement(uuid, request)

        // then
        coVerify(exactly = 1) { repository.save(any()) }
        assertEquals(
            "00000000-0000-0000-0000-000000000000,tag=value field=10i",
            capturedPointSlot.captured.toLineProtocol(),
        )
    }
}
