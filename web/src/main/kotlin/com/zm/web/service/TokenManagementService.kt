package com.zm.web.service

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class TokenManagementService {

    private val tokenBlacklist = ConcurrentHashMap<String, Boolean>()

    fun invalidateToken(token: String) {
        tokenBlacklist[token] = true
    }

    fun isTokenValid(token: String): Boolean {
        return !tokenBlacklist.containsKey(token)
    }
}