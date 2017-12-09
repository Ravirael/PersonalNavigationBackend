package pl.polsl.geomarkers.authentication

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthenticationDetailsAdapter(private val authenticationData: AuthenticationData): UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        //TODO: no idea
        return mutableListOf<GrantedAuthority>(SimpleGrantedAuthority("USER"))
    }

    override fun isEnabled() = true

    override fun getUsername() = authenticationData.id.toString()

    override fun isCredentialsNonExpired() = true

    override fun getPassword() = authenticationData.key.toString()

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true
}