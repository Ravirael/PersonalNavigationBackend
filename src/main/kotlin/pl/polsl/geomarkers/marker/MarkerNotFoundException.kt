package pl.polsl.geomarkers.marker

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class MarkerNotFoundException(id: Long): RuntimeException("Marker $id not found!")