package com.zm.web.repository

import com.zm.web.repository.data.Cart
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CartRepository : JpaRepository<Cart, UUID>