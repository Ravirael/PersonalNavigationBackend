package pl.polsl.geomarkers.marker

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class JpaMarker(
        @Id
        override val id: Long,

        override val name: String,

        position: GeoPoint,

        updateTimestamp: Instant
) : IdentifiableMarker {
    @Embedded
    override val position: DefaultGeoPoint = DefaultGeoPoint(
            longitude = position.longitude,
            latitude = position.latitude,
            altitude = position.altitude
    )

    private val sqlUpdateTimestamp = java.sql.Timestamp.from(updateTimestamp)

    @get:JsonIgnore
    val updateTimestamp: Instant
        get() = sqlUpdateTimestamp.toInstant()

}