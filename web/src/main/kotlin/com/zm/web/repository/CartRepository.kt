package com.zm.web.repository

import com.zm.web.repository.data.Cart
import org.springframework.data.jpa.repository.JpaRepository

interface CartRepository : JpaRepository<Cart, Long>