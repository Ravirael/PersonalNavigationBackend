package pl.polsl.geomarkers.authentication

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.Principal
import java.util.*

@Service
class DefaultAuthenticationService(
        private val passwordEncoder: PasswordEncoder
) : AuthenticationService {
    private var currentId = 0L
    private val authentitcations = mutableMapOf<Long, AuthenticationData>()

    override fun findById(id: Long): AuthenticationData {
        return authentitcations[id] ?: throw UsernameNotFoundException("There is no user with $id!")
    }

    override fun generateAuthenticationData(): AuthenticationData {
        val authentication = AuthenticationData(currentId, UUID.randomUUID().toString())
        authentitcations.put(currentId, AuthenticationData(currentId, passwordEncoder.encode(authentication.key)))
        ++currentId
        return authentication
    }

    override fun principalId(principal: Principal): Long {
        val id = principal.name.toLong()
        return if (authentitcations.containsKey(id)) {
            id
        } else {
            throw UsernameNotFoundException("$id is invalid!")
        }
    }
}