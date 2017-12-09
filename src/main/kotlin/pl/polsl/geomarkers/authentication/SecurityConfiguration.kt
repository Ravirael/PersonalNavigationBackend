package pl.polsl.geomarkers.authentication

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.access.channel.ChannelProcessingFilter
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
class SecurityConfiguration(
        @Autowired
        private val userDetailsService: UserDetailsService,

        @Autowired
        private val passwordEncoder: PasswordEncoder
): WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder) {
        super.configure(auth)
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity) {
        http
                //.addFilterBefore(CORSFilter(), ChannelProcessingFilter::class.java)

                .formLogin()
                .successHandler(RestAuthenticationSuccessHandler())
                .failureHandler(RestAuthenticationFailureHandler())
                .usernameParameter("id")
                .passwordParameter("key")
                .loginPage("/authenticate")
                .and()
                .addFilter(
                        CustomUsernamePasswordAuthenticationFilter(
                                authenticationManager(),
                                RestAuthenticationSuccessHandler()
                        )
                )
                .csrf().disable()
                .cors().disable()
                .exceptionHandling()
                .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
    }
}