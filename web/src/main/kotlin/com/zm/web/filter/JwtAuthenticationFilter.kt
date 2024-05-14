package com.zm.web.filter

import com.zm.web.utils.JwtTokenUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    @Value("\${jwt.key}") private val jwtKey: String
) : OncePerRequestFilter() {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        requestTokenHeader?.takeIf { it.startsWith(BEARER_PREFIX) }?.let {
            val jwtToken = it.removePrefix(BEARER_PREFIX)
            if (JwtTokenUtils.validateToken(jwtToken, jwtKey)) {
                val claims = JwtTokenUtils.getAllClaimsFromToken(jwtToken, jwtKey)
                val account = claims.subject
                val authorities = listOf(SimpleGrantedAuthority("MEMBER"))

                val userDetails = User(account, "", authorities)
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                ).apply {
                    details = WebAuthenticationDetailsSource().buildDetails(request)
                }
                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)
    }
}
