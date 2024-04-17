package com.zm.web.model.request

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank
    val account: String,

    @field:NotBlank
    val password: String
)
