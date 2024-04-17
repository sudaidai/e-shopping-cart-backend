package com.zm.web.controller

import com.zm.web.repository.MemberRepository
import com.zm.web.repository.data.Member
import com.zm.web.model.request.RegistrationRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

// This annotation marks this class as a REST controller
@RestController
@RequestMapping("/api") // Base path for this controller
class MemberController(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder // Injecting PasswordEncoder for password encryption
) {

    // This annotation maps HTTP POST requests to "/api/member" to this method
    @PostMapping("/member")
    @ResponseStatus(HttpStatus.CREATED)
    // @Valid ensures that the incoming request body is validated against the constraints specified in the Registration class
    fun registerMember(@Valid @RequestBody registrationRequest: RegistrationRequest) {
        // Create a new Member instance using the name from the Registration request body
        // Encrypt the password before saving it to the database
        val member = Member(
            account = registrationRequest.account,
            password = passwordEncoder.encode(registrationRequest.password) // Encoding password
        )

        // Save the member using the injected memberRepository
        memberRepository.save(member)
    }
}