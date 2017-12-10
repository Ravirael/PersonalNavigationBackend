package pl.polsl.geomarkers.marker

import pl.polsl.geomarkers.GenerateNoArg

@GenerateNoArg
data class DefaultGeoPoint(
        override val longitude: Double,
        override val latitude: Double,
        override val altitude: Double
) : GeoPoint