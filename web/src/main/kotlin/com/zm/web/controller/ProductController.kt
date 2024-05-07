package com.zm.web.controller

import com.zm.web.model.ProductDTO
import com.zm.web.model.fromProduct
import com.zm.web.repository.ProductRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/product")
class ProductController (
    private val productRepository: ProductRepository
){
    @GetMapping
    fun listProduct(request: HttpServletRequest): List<ProductDTO>{
        val baseUrl = "${request.scheme}://${request.serverName}:${request.serverPort}/"
        return productRepository.findAll().stream()
            .map { p -> fromProduct(p, baseUrl) }
            .toList()
    }
}