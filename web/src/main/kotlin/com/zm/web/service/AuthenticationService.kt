package com.zm.web.service

import com.zm.web.utils.JwtTokenUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class AuthenticationService(
    @Value("\${jwt.key}") private val jwtKey: String,
    private val authManager: AuthenticationManager
) {

    fun authenticate(account: String, password: String): String {
        return try {
            authManager.authenticate(UsernamePasswordAuthenticationToken(account, password))
            JwtTokenUtils.generateToken(account, emptyMap(), jwtKey)
        } catch (e: BadCredentialsException) {
            throw BadCredentialsException("INVALID_CREDENTIALS -> $account")
        } catch (e: InternalAuthenticationServiceException) {
            throw IllegalArgumentException("BAD_REQUEST", e)
        }
    }
}
