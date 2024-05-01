package com.zm.web.model.response

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.zm.web.model.ProductDTO
import com.zm.web.serializer.CustomSerializer

@JsonSerialize(using = CustomSerializer::class)
data class HomeResponse(
    val name: String,
    val products: List<ProductDTO>,
    val cartId: Long?,
    val itemsCount: Int,
    val isCartEmpty: Boolean
)