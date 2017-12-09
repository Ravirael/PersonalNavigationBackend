package pl.polsl.geomarkers.authentication

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthenticationDetailsService(
        private val authenticationService: AuthenticationService
): UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
        return AuthenticationDetailsAdapter(
                authenticationService.findById(id.toLong())
        )
    }

}