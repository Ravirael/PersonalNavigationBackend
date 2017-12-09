package pl.polsl.geomarkers.authentication

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class DataFormatException(message: String?, cause: Throwable? = null) : Exception(message, cause)