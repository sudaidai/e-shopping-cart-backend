package com.zm.web.model.request

import jakarta.validation.constraints.NotBlank

data class RegistrationRequest(
    @field:NotBlank
    val account: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    val name: String
)
