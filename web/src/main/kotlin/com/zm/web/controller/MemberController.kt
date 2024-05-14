package com.zm.web.controller

import com.zm.web.repository.MemberRepository
import com.zm.web.repository.data.Member
import com.zm.web.model.request.RegistrationRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member")
class MemberController(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registerMember(@Valid @RequestBody registrationRequest: RegistrationRequest) {
        val member = Member(
            account = registrationRequest.account,
            password = passwordEncoder.encode(registrationRequest.password),
            name = registrationRequest.name
        )
        memberRepository.save(member)
    }
}