package pl.polsl.geomarkers.marker

import java.util.*

interface Marker {
    val name: String
    val position: GeoPoint
}