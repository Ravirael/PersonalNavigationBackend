package pl.polsl.geomarkers.authentication

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(
        private val authenticationService: AuthenticationService
) {

    @RequestMapping(value = ["/authentication"], method = [RequestMethod.POST])
    fun generateNewUserId(): AuthenticationData {
        return authenticationService.generateAuthenticationData()
    }
}

