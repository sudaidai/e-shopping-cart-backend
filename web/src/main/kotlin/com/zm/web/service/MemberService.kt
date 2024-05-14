package com.zm.web.service

import com.zm.web.repository.MemberRepository
import com.zm.web.repository.data.Member
import com.zm.web.utils.SecurityUtils
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun getCurrentMember(): Member? {
        val currentAccount = SecurityUtils.getCurrentUser()
        return memberRepository.findByAccount(currentAccount)
    }
}
