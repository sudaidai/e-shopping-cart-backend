package com.zm.web.controller

import com.zm.web.model.request.RegistrationRequest
import com.zm.web.repository.MemberRepository
import com.zm.web.repository.data.Member
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

    @PostMapping
    fun registerMember(@Valid @RequestBody registrationRequest: RegistrationRequest): ResponseEntity<String> {
        return if (memberRepository.existsByAccount(registrationRequest.account)) {
            ResponseEntity.status(HttpStatus.CONFLICT).body("Account (Email) already exists.")
        } else {
            val member = Member(
                account = registrationRequest.account,
                password = passwordEncoder.encode(registrationRequest.password),
                name = registrationRequest.name
            )
            memberRepository.save(member)
            ResponseEntity.status(HttpStatus.CREATED).body("Registration successful.")
        }
    }
}
