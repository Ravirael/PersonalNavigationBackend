package pl.polsl.geomarkers.marker

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.annotations.ApiParam
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
    fun getMarkers(
            principal: Principal?,

            @ApiParam(
                    value = """JSON containing a valid bounding box eg. `{"north":50, "south": 40, "west": 30, "east": 20}`""",
                    example = """{"north":50, "south": 40, "west": 30, "east": 20}""",
                    required = false
            )
            @RequestParam(required = false)
            boundingBox: Optional<String>,

            @RequestParam(required = false, defaultValue = "")
            genders: List<Gender>,

            @RequestParam(required = false, defaultValue = "")
            skills: List<Skill>
    ): List<IdentifiableMarker> {
        val mappedBoundingBox =  boundingBox
                .map { ObjectMapper().readValue(it, DefaultBoundingBox::class.java) as BoundingBox }

        return markerService.filteredMarkersIn(
                userId = Optional.ofNullable(principal).map { authenticationService.principalId(it) },
                bounds = mappedBoundingBox,
                genders = if (genders.isEmpty()) EnumSet.allOf(Gender::class.java) else EnumSet.copyOf(genders),
                skills = if (skills.isEmpty()) EnumSet.allOf(Skill::class.java) else EnumSet.copyOf(skills)
        )
    }

    @RequestMapping(value = ["/markers/sorted"], method = [RequestMethod.GET])
    fun getFilteredSortedMarkers(
            principal: Principal?,

            @RequestParam(required = false, defaultValue = "")
            genders: List<Gender>,

            @RequestParam(required = false, defaultValue = "")
            skills: List<Skill>,

            @RequestParam
            limit: Int

    ): List<IdentifiableMarker> {
        val id = authenticationService.principalId(principal!!)

        return markerService.closestMarkers(
                id = id,
                genders = if (genders.isEmpty()) EnumSet.allOf(Gender::class.java) else EnumSet.copyOf(genders),
                skills = if (skills.isEmpty()) EnumSet.allOf(Skill::class.java) else EnumSet.copyOf(skills),
                limit = limit
        )
    }

    @RequestMapping(value = ["/marker/{id}"], method = [RequestMethod.GET])
    fun getMarker(@PathVariable id: Long): IdentifiableMarker {
        return markerService.marker(id)
    }

}

