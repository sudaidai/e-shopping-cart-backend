package com.zm.web.request

import jakarta.validation.constraints.NotBlank

data class RegistrationRequest(
    @field:NotBlank
    val account: String,

    @field:NotBlank
    val password: String
)
