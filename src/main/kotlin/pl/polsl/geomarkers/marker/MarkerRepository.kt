package pl.polsl.geomarkers.marker

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface MarkerRepository: JpaRepository<JpaMarker, Long> {
    fun findByPositionLatitudeBetweenAndPositionLongitudeBetween(
            lowerLatitude: Double,
            upperLatitude: Double,
            lowerLongitude: Double,
            upperLongitude: Double
    ): List<JpaMarker>
}