package com.zm.web.controller

import com.zm.web.model.request.RegistrationRequest
import com.zm.web.repository.MemberRepository
import com.zm.web.repository.data.Member
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member")
class MemberController(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Operation(summary = "Register a new member")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Registration successful",
            content = [Content(mediaType = "text/plain", schema = Schema(implementation = String::class))]),
        ApiResponse(responseCode = "409", description = "Account (Email) already exists",
            content = [Content(mediaType = "text/plain", schema = Schema(implementation = String::class))])
    ])
    @PostMapping
    fun registerMember(@Valid @RequestBody registrationRequest: RegistrationRequest): ResponseEntity<String> {
        return if (memberRepository.existsByAccount(registrationRequest.account)) {
            ResponseEntity.status(HttpStatus.CONFLICT).body("Account (Email) already exists.")
        } else {
            val member = Member(
                account = registrationRequest.account,
                password = passwordEncoder.encode(registrationRequest.password)
            )
            memberRepository.save(member)
            ResponseEntity.status(HttpStatus.CREATED).body("Registration successful.")
        }
    }
}
