package com.zm.web.utils

import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {
    fun getCurrentUser(): String {
        val principal = SecurityContextHolder.getContext().authentication.principal
        return (principal as? String) ?: ""
    }
}