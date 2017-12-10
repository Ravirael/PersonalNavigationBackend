package pl.polsl.geomarkers.marker

import org.springframework.stereotype.Service
import java.time.Clock

@Service
class DefaultMarkerService(
        private val markerRepository: MarkerRepository,
        private val clock: Clock
): MarkerService {
    override fun updateMarker(id: Long, marker: Marker) {
        val jpaMarker = JpaMarker(
                id = id,
                name = marker.name,
                position = marker.position,
                updateTimestamp = clock.instant()
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
}