package pl.polsl.geomarkers.authentication

import pl.polsl.geomarkers.authentication.AuthenticationData
import java.security.Principal
import java.util.*

interface AuthenticationService {
    fun generateAuthenticationData(): AuthenticationData
    fun findById(id: Long): AuthenticationData
    fun principalId(principal: Principal): Long
}