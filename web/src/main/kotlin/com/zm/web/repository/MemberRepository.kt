package com.zm.web.repository

import com.zm.web.repository.data.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {

    fun findByName(name: String): Member?
}