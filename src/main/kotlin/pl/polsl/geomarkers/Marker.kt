package pl.polsl.geomarkers

import java.util.*

interface Marker {
    val id: UUID
    val name: String
    val position: GeoPoint
}