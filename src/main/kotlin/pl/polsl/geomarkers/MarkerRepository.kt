package pl.polsl.geomarkers

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface MarkerRepository: JpaRepository<JpaMarker, UUID>