package pl.polsl.geomarkers.marker

import pl.polsl.geomarkers.GenerateNoArg

@GenerateNoArg
data class DefaultMarker(
        override val name: String,
        override val position: DefaultGeoPoint,
        override val gender: Gender,
        override val skill: Skill
) : Marker