package com.zm.web.service

import com.zm.web.exception.BusinessException
import com.zm.web.model.response.CartResponse
import com.zm.web.repository.CartRepository
import com.zm.web.repository.ItemRepository
import com.zm.web.repository.data.Cart
import com.zm.web.repository.data.Item
import com.zm.web.repository.data.Member
import com.zm.web.repository.data.Product
import com.zm.web.utils.SecurityUtils
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val memberService: MemberService
) {

    fun queryCart(id: String): CartResponse {
        val cart = cartRepository.findById(id.toLong()).orElseThrow {
            NoSuchElementException("Cart with ID $id not found")
        }

        return prepareCartResponse(cart)
    }

    fun addItemToCart(productId: String, quantity: Int): CartResponse {
        val member = memberService.getCurrentMember()
            ?: throw BusinessException("Unauthorized")

        var cart = cartRepository.findByMember(member) ?: Cart(member = member).also {
            cartRepository.save(it)
        }

        cart.cartItems.add(Item(cart = cart, member = member, product = Product(productId.toLong()), quantity = quantity))
        cart = cartRepository.save(cart)
        return prepareCartResponse(cart)

    }

    fun updateCartItem(cartItemId: String, quantity: Int): CartResponse {
        val member = memberService.getCurrentMember()
            ?: throw BusinessException("Unauthorized")

        var cart = cartRepository.findByMember(member)
            ?: throw BusinessException("No cart exist. Unable to update cart")

        for (i in cart.cartItems.indices) {
            if (cart.cartItems[i].id == cartItemId.toLong()) {
                cart.cartItems[i].quantity += quantity
                cart = cartRepository.save(cart)
            }
        }

        return prepareCartResponse(cart)
    }

    private fun prepareCartResponse(cart: Cart): CartResponse {
        val currency = cart.currency!!
        val subTotal = calculateSubTotal(cart.cartItems)
        val shippingTotal = calculateShippingTotal(cart.cartItems)
        val taxTotal = calculateTaxTotal(subTotal)
        val grandTotal = subTotal + shippingTotal + taxTotal

        return cart.toCartResponse(currency, subTotal, shippingTotal, taxTotal, grandTotal)
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
