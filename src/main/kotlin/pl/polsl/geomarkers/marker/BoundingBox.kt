package pl.polsl.geomarkers.marker

interface BoundingBox {
    val latitudeRange: ClosedRange<Double>
    val longitudeRange: ClosedRange<Double>
}

fun BoundingBox.contains(point: GeoPoint): Boolean {
    return point.latitude in latitudeRange
            && point.longitude in longitudeRange
}