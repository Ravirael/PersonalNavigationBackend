package pl.polsl.geomarkers.marker

interface GeoPoint {
    val latitude: Double
    val longitude: Double
    val altitude: Double
}

fun GeoPoint.distanceTo(other: GeoPoint): Double {
    val DEG2RAD = (Math.PI / 180.0)
    val RADIUS_EARTH_METERS = 6378137.0

    val a1 = DEG2RAD * this.latitude
    val a2 = DEG2RAD * this.longitude
    val b1 = DEG2RAD * other.latitude
    val b2 = DEG2RAD * other.longitude

    val cosa1 = Math.cos(a1)
    val cosb1 = Math.cos(b1)

    val t1 = cosa1 * Math.cos(a2) * cosb1 * Math.cos(b2)

    val t2 = cosa1 * Math.sin(a2) * cosb1 * Math.sin(b2)

    val t3 = Math.sin(a1) * Math.sin(b1)

    val tt = Math.acos(t1 + t2 + t3)

    return RADIUS_EARTH_METERS * tt
}