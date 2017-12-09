package pl.polsl.geomarkers.authentication

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException


class CORSFilter : Filter {

    private val log = LoggerFactory.getLogger(CORSFilter::class.java)

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
        log.info("CORSFilter init")
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin") ?: "all")
        response.setHeader("Access-Control-Allow-Credentials", "true")
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
        response.setHeader("Access-Control-Max-Age", "3600")
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me")

        filterChain.doFilter(servletRequest, servletResponse)
    }

    override fun destroy() {
        log.info("CORSFilter destruction")
    }
}
