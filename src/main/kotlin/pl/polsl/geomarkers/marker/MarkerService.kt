package pl.polsl.geomarkers.marker

import java.util.*

interface MarkerService {
    fun updateMarker(id: Long, marker: Marker)
    fun deleteMarker(id: Long)
    fun marker(id: Long): IdentifiableMarker
    fun markers(): List<IdentifiableMarker>
    fun markersIn(bounds: BoundingBox): List<IdentifiableMarker>

    fun filteredMarkersIn(
            userId: Optional<Long>,
            bounds: Optional<BoundingBox>,
            genders: EnumSet<Gender>,
            skills: EnumSet<Skill>
    ): List<IdentifiableMarker>

    fun closestMarkers(
            id: Long,
            genders: EnumSet<Gender>,
            skills: EnumSet<Skill>,
            limit: Int
    ): List<DistanceMarker>
}