package com.zm.web.repository

import com.zm.web.repository.data.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>