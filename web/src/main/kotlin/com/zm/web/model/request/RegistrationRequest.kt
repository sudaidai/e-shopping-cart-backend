package com.zm.web.model.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class RegistrationRequest(
    @field:NotBlank(message = "Account is mandatory")
    val account: String,

    @field:NotBlank(message = "Password is mandatory")
    val password: String,

    @field:NotBlank(message = "Email is mandatory")
    @field:Email(message = "Email should be valid")
    var email: String,

    @field:NotBlank(message = "Name is mandatory")
    var name: String,

    @field:NotBlank(message = "Country is mandatory")
    var country: String,

    @field:NotBlank(message = "Address is mandatory")
    var address: String,

    @field:NotBlank(message = "Phone is mandatory")
    @field:Pattern(regexp = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}\$", message = "Phone number is invalid")
    var phone: String,
)
