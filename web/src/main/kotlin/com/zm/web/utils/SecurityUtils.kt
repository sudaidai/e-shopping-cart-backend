package com.zm.web.utils

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

object SecurityUtils {
    fun getCurrentUser(): String {
        val principal = SecurityContextHolder.getContext().authentication.principal
        if (principal is User) {
            return principal.username
        }
        return ""
    }
}