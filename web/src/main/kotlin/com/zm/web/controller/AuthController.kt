package com.zm.web.controller

import com.zm.web.request.LoginRequest
import com.zm.web.service.AuthenticationService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController (
    private val authenticationService: AuthenticationService
){

    @Operation(summary = "Authenticate with account and password.")
    @PostMapping
    fun login(@Valid @RequestBody loginRequest: LoginRequest): String? {
        return authenticationService.authenticate(loginRequest.account, loginRequest.password)
    }
}