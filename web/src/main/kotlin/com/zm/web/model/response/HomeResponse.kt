package com.zm.web.model.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.zm.web.model.ProductDTO

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class HomeResponse(
    val name: String,
    val products: List<ProductDTO>,
    val cartId: Long?,
    val itemsCount: Int,
    val isCartEmpty: Boolean
)