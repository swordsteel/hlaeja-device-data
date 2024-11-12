package ltd.hlaeja.controller

import java.util.UUID
import ltd.hlaeja.library.deviceData.MeasurementData
import ltd.hlaeja.service.MeasurementService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class MeasurementController(
    private val service: MeasurementService,
) {

    @PostMapping("/client-{client}")
    @ResponseStatus(CREATED)
    suspend fun addMeasurement(
        @PathVariable client: UUID,
        @RequestBody measurement: MeasurementData.Request,
    ) = service.addMeasurement(client, measurement)

    @GetMapping("/client-{client}/node-{node}")
    suspend fun getLastDeviceMeasurement(
        @PathVariable client: UUID,
        @PathVariable node: UUID,
    ): MeasurementData.Response = service.getNodeMeasurement(client, node)
}
