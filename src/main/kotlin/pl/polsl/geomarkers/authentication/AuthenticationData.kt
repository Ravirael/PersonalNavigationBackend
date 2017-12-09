package pl.polsl.geomarkers.authentication

import pl.polsl.geomarkers.GenerateNoArg
import java.util.*

@GenerateNoArg
data class AuthenticationData(
        val id: Long,
        val key: String
)