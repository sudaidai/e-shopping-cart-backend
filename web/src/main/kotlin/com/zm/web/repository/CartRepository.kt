package com.zm.web.repository

import com.zm.web.repository.data.Cart
import com.zm.web.repository.data.Member
import org.springframework.data.jpa.repository.JpaRepository

interface CartRepository : JpaRepository<Cart, Long> {

    fun findByMember(member: Member): Cart?
}