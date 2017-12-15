package pl.polsl.geomarkers.marker

interface MarkerService {
    fun updateMarker(id: Long, marker: Marker)
    fun deleteMarker(id: Long)
    fun marker(id: Long): IdentifiableMarker
    fun markers(): List<IdentifiableMarker>
    fun markersIn(bounds: BoundingBox): List<IdentifiableMarker>
}