package pl.polsl.geomarkers.authentication

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomUsernamePasswordAuthenticationFilter(
        authenticationManager: AuthenticationManager,
        successHandler: AuthenticationSuccessHandler
) : UsernamePasswordAuthenticationFilter() {

    init {
        this.authenticationManager = authenticationManager
        this.setAuthenticationSuccessHandler(successHandler)
        this.setRequiresAuthenticationRequestMatcher {
            it.requestURI == "/authenticate"
        }
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {

        return if (request.contentType?.startsWith("application/json") == true) {
            try {
                val userData = ObjectMapper().readValue(request.reader, AuthenticationData::class.java)
                val authRequest = UsernamePasswordAuthenticationToken(userData.id, userData.key)
                this.setDetails(request, authRequest)
                this.authenticationManager.authenticate(authRequest)
            } catch (e: JsonProcessingException) {
                throw DataFormatException(e.message, e)
            }
        } else {
            super.attemptAuthentication(request, response)
        }
    }

}