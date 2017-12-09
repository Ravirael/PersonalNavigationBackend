package pl.polsl.geomarkers.authentication

import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest


class RestAuthenticationSuccessHandler : AuthenticationSuccessHandler {
    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
            request: HttpServletRequest,
            response: HttpServletResponse,
            auth: Authentication
    ) {
        response.status = HttpStatus.OK.value()
    }
}