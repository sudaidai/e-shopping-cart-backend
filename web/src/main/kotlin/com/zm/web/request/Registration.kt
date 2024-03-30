package com.zm.web.request

import jakarta.validation.constraints.NotBlank

data class Registration(
    @field:NotBlank
    val name: String,

    @field:NotBlank
    val password: String
)
