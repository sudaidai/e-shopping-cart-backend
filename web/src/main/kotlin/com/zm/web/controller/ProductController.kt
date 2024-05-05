package com.zm.web.controller

import com.zm.web.model.ProductDTO
import com.zm.web.model.fromProduct
import com.zm.web.repository.ProductRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/api/product")
class ProductController (
    private val productRepository: ProductRepository
){
    @GetMapping
    fun listProduct(): List<ProductDTO>{
        return productRepository.findAll().stream()
            .map { p -> fromProduct(p) }
            .toList()
    }
}