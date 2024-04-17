package com.zm.web.service

import com.zm.web.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService (
    private val productRepository: ProductRepository
){
}