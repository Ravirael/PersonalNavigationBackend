package pl.polsl.geomarkers.marker

interface Marker {
    val name: String
    val position: GeoPoint
    val gender: Gender
    val skill: Skill
}