package com.zm.web.model.response

import java.math.BigDecimal

data class CartResponse(
    val id: String,
    val email: String?,
    val isEmpty: Boolean,
    val abandoned: Boolean,
    val totalItems: Int,
    val totalUniqueItems: Int,
    val currency: CurrencyDTO,
    val subTotal: PriceDTO,
    val shippingTotal: PriceDTO,
    val taxTotal: PriceDTO,
    val grandTotal: PriceDTO,
    val attributes: List<AttributeDTO>,
    val notes: String?,
    val createdAt: String,
    val updatedAt: String,
    val items: List<ItemDTO>
)

data class ItemDTO(
    val id: String,
    val name: String,
    val description: String?,
    val images: List<String>,
    val quantity: Int,
    val attributes: List<AttributeDTO>,
    val unitTotal: PriceDTO,
    val lineTotal: PriceDTO,
    val createdAt: String,
    val updatedAt: String
)

data class CurrencyDTO(
    val code: String,
    val symbol: String?
)

data class PriceDTO(
    val amount: BigDecimal,
    val formatted: String
)

data class AttributeDTO(
    val key: String,
    val value: String
)
