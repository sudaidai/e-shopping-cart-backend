package com.zm.web.utils

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

object SecurityUtils {
    fun getCurrentUser(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication.principal

        return if (principal is User) {
            principal.username
        } else {
            ""
        }
    }
}
