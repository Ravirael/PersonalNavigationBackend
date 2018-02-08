package pl.polsl.geomarkers.marker

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.util.*

@Service
@Transactional
class DefaultMarkerService(
        private val markerRepository: MarkerRepository,
        private val clock: Clock
): MarkerService {
    override fun updateMarker(id: Long, marker: Marker) {
        val jpaMarker = JpaMarker(
                id = id,
                name = marker.name,
                position = marker.position,
                updateTimestamp = clock.instant(),
                gender = marker.gender,
                skill = marker.skill
        )
        if (markerRepository.exists(id)) {
            markerRepository.delete(id)
        }
        markerRepository.save(jpaMarker)
    }

    override fun deleteMarker(id: Long) {
        markerRepository.delete(markerRepository.findOne(id))
    }

    override fun markers(): List<IdentifiableMarker> {
        return markerRepository.findAll()
    }

    override fun markersIn(bounds: BoundingBox): List<IdentifiableMarker> {
        return markerRepository.findByPositionLatitudeBetweenAndPositionLongitudeBetween(
                lowerLatitude = bounds.latitudeRange.start,
                upperLatitude = bounds.latitudeRange.endInclusive,
                lowerLongitude = bounds.longitudeRange.start,
                upperLongitude = bounds.longitudeRange.endInclusive
        )
    }

    override fun marker(id: Long): IdentifiableMarker {
        return markerRepository.findOne(id) ?: throw MarkerNotFoundException(id)
    }

    override fun filteredMarkersIn(
            userId: Optional<Long>,
            bounds: Optional<BoundingBox>,
            genders: EnumSet<Gender>,
            skills: EnumSet<Skill>
    ): List<IdentifiableMarker> {
        return bounds
                .map { markersIn(it) }
                .orElseGet { markers() }
                .filter { marker ->
                    userId.map { it == marker.id  }.orElse(false) ||
                            (marker.gender in genders && marker.skill in skills)
                }
    }

    override fun closestMarkers(
            id: Long,
            genders: EnumSet<Gender>,
            skills: EnumSet<Skill>,
            limit: Int
    ): List<DistanceMarker> {
        val userMarker = marker(id)

        return filteredMarkersIn(
                userId = Optional.empty(),
                bounds = Optional.empty(),
                genders = genders,
                skills = skills
        )
                .map { DistanceMarker(it, it.position.distanceTo(userMarker.position)) }
                .sortedBy { it.distance }
                .filter { it.id != id }
                .take(limit)
    }
}
