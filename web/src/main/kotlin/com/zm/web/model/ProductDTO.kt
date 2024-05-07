package com.zm.web.model

import com.zm.web.repository.data.Product
import java.math.BigDecimal

data class ProductDTO(
    val id: String,
    val name: String,
    val price: BigDecimal,
    val description: String,
    val images: List<String>
)

fun fromProduct(product: Product, baseUrl: String): ProductDTO {
    val imagePaths = product.images?.split(",")?.map { "$baseUrl${it.trim()}" } ?: emptyList()

    return ProductDTO(
        id = product.id.toString(),
        name = product.name ?: throw IllegalArgumentException("Product name cannot be null"),
        price = product.price ?: BigDecimal.ZERO,
        description = product.description ?: "",
        images = imagePaths
    )
}

