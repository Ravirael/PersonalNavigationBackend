package pl.polsl.geomarkers.marker

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import pl.polsl.geomarkers.authentication.AuthenticationService
import java.security.Principal
import java.util.*

@RestController
class MarkerController(
        private val authenticationService: AuthenticationService,
        private val markerService: MarkerService
) {

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = ["/markers"], method = [RequestMethod.POST, RequestMethod.PUT])
    fun updateMarker(principal: Principal?, @RequestBody marker: DefaultMarker) {
        val id = authenticationService.principalId(principal!!)
        markerService.updateMarker(id, marker)
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = ["/markers"], method = [RequestMethod.DELETE])
    fun removeMarker(principal: Principal?) {
        val id = authenticationService.principalId(principal!!)
        markerService.deleteMarker(id)
    }

    @RequestMapping(value = ["/markers"], method = [RequestMethod.GET])
    fun getMarkers(@RequestParam(required = false) boundingBox: Optional<String>): List<IdentifiableMarker> {
        return boundingBox
                .map { ObjectMapper().readValue(it, DefaultBoundingBox::class.java) }
                .map { markerService.markersIn(it) }
                .orElseGet { markerService.markers() }
    }

    @RequestMapping(value = ["/marker/{id}"], method = [RequestMethod.GET])
    fun getMarker(@PathVariable id: Long): IdentifiableMarker {
        return markerService.marker(id)
    }

}

