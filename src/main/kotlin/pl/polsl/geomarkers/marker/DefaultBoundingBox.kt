package pl.polsl.geomarkers.marker

import com.fasterxml.jackson.annotation.JsonIgnore
import pl.polsl.geomarkers.GenerateNoArg

@GenerateNoArg
data class DefaultBoundingBox(
        val north: Double,
        val east: Double,
        val south: Double,
        val west: Double
) : BoundingBox {
    @get:JsonIgnore
    override val latitudeRange: ClosedRange<Double>
        get() = south..north

    @get:JsonIgnore
    override val longitudeRange: ClosedRange<Double>
        get() = west..east
}