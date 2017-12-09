package pl.polsl.geomarkers

@GenerateNoArg
data class DefaultGeoPoint(
        override val longitude: Double,
        override val latitude: Double,
        override val altitude: Double
) : GeoPoint