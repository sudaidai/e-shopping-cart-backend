package com.zm.web.service

import com.zm.web.constant .Currency
import com.zm.web.exception.BusinessException
import com.zm.web.model.response.*
import com.zm.web.repository.CartRepository
import com.zm.web.repository.data.Item
import com.zm.web.resolver.CurrencyInput
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class CartService(
    private val cartRepository: CartRepository
) {

    fun queryCart(id: UUID, currencyInput: CurrencyInput): CartResponse {
        val cart = cartRepository.findById(id).orElseThrow {
            NoSuchElementException("Cart with ID $id not found")
        }

        val codeInput = currencyInput.code
        val currency = Currency.fromCode(codeInput) ?:
            throw BusinessException("Currency not supported with code $codeInput")

        val subTotal = calculateSubTotal(cart.cartItems)
        val shippingTotal = calculateShippingTotal(cart.cartItems)
        val taxTotal = calculateTaxTotal(subTotal)
        val grandTotal = subTotal.add(shippingTotal).add(taxTotal)

        return CartResponse(
            id = cart.id!!,
            email = cart.member?.email ?: "",
            isEmpty = cart.cartItems.isEmpty(),
            abandoned = cart.isAbandoned,
            totalItems = cart.cartItems.sumOf { it.quantity },
            totalUniqueItems = cart.cartItems.size,
            currency = CurrencyDTO(currency.name, currency.getSymbol()),
            subTotal = PriceDTO(subTotal, currency.getSymbol() + subTotal.toPlainString()),
            shippingTotal = PriceDTO(shippingTotal, currency.getSymbol() + shippingTotal.toPlainString()),
            taxTotal = PriceDTO(taxTotal, currency.getSymbol() + taxTotal.toPlainString()),
            grandTotal = PriceDTO(grandTotal, currency.getSymbol() + grandTotal.toPlainString()),
            attributes = listOf(),
            notes = cart.notes,
            createdAt = cart.createTime?.toString() ?: "",
            updatedAt = cart.updateTime?.toString() ?: "",
            items = cart.cartItems.map { it.toItemDTO(currency) }
        )
    }

    private fun calculateSubTotal(items: List<Item>): BigDecimal {
        return items.sumOf { item ->
            val price = item.product?.price ?: BigDecimal.ZERO
            price.multiply(BigDecimal(item.quantity))
        }
    }

    private fun calculateShippingTotal(items: List<Item>): BigDecimal {
        return BigDecimal(items.size)
    }

    private fun calculateTaxTotal(subTotal: BigDecimal): BigDecimal {
        return subTotal.multiply(BigDecimal(0.1))
    }
}
