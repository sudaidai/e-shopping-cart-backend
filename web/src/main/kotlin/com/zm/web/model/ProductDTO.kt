package com.zm.web.model

import com.zm.web.repository.data.Product
import java.math.BigDecimal
import java.util.*

data class ProductDTO(
    val id: UUID,
    val name: String,
    val price: BigDecimal,
    val description: String,
    val images: List<String>
)

fun fromProduct(product: Product): ProductDTO {
    return ProductDTO(
        id = product.id ?: throw IllegalArgumentException("Product ID cannot be null"),
        name = product.name ?: throw IllegalArgumentException("Product name cannot be null"),
        price = product.price ?: BigDecimal.ZERO,
        description = product.description ?: "",
        images = product.images?.split(",")?.map(String::trim) ?: emptyList()
    )
}

