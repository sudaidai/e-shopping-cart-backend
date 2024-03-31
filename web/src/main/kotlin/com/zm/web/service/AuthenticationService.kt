package com.zm.web.service

import com.zm.web.utils.JwtTokenUtils
import org.apache.coyote.BadRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    @Value("\${jwt.key}") private val jwtKey: String = "123456",
    private val authManager: AuthenticationManager
) {
    fun authenticate(account: String, password: String): String? {
        try {
            authManager.authenticate(UsernamePasswordAuthenticationToken(account, password))
        } catch (e: BadCredentialsException) {
            throw BadCredentialsException("INVALID_CREDENTIALS -> $account")
        } catch (e: InternalAuthenticationServiceException) {
            throw BadRequestException("BAD_REQUEST")
        }

        return JwtTokenUtils.generateToken(account, emptyMap(), jwtKey)
    }
}