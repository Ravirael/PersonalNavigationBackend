package pl.polsl.geomarkers

import java.util.*
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class JpaMarker(
        @Id
        override val id: UUID,

        override val name: String,

        position: GeoPoint) : Marker {
    @Embedded
    override val position: DefaultGeoPoint = DefaultGeoPoint(position.longitude, position.latitude, position.altitude)

}