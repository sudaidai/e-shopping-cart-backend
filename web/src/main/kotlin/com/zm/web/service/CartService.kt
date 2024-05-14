package com.zm.web.service

import com.zm.web.exception.BusinessException
import com.zm.web.model.response.CartResponse
import com.zm.web.repository.CartRepository
import com.zm.web.repository.data.Cart
import com.zm.web.repository.data.Item
import com.zm.web.repository.data.Product
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

        val cart = cartRepository.findByMember(member) ?: Cart(member = member).apply {
            cartRepository.save(this)
        }

        cart.cartItems.add(Item(cart = cart, member = member, product = Product(productId.toLong()), quantity = quantity))
        cartRepository.save(cart)
        return prepareCartResponse(cart)
    }

    fun updateCartItem(cartItemId: String, quantity: Int): CartResponse {
        val member = memberService.getCurrentMember()
            ?: throw BusinessException("Unauthorized")

        val cart = cartRepository.findByMember(member)
            ?: throw BusinessException("No cart exist. Unable to update cart")

        val cartItem = cart.cartItems.find { it.id == cartItemId.toLong() }
            ?: throw BusinessException("Item not found in cart")

        cartItem.quantity += quantity
        cartRepository.save(cart)
        return prepareCartResponse(cart)
    }

    fun removeCartItem(cartItemId: String): CartResponse {
        val member = memberService.getCurrentMember()
            ?: throw BusinessException("Unauthorized")

        val cart = cartRepository.findByMember(member)
            ?: throw BusinessException("No cart exist. Unable to delete item from cart")

        val cartItem = cart.cartItems.find { it.id == cartItemId.toLong() }
            ?: throw BusinessException("Item not found in cart")

        cart.cartItems.remove(cartItem)
        cartRepository.save(cart)
        return prepareCartResponse(cart)
    }

    fun clearCart(): CartResponse {
        val member = memberService.getCurrentMember()
            ?: throw BusinessException("Unauthorized")

        val cart = cartRepository.findByMember(member)
            ?: throw BusinessException("No cart exist.")

        cart.cartItems.clear()
        cartRepository.save(cart)
        return prepareCartResponse(cart)
    }

    private fun prepareCartResponse(cart: Cart): CartResponse {
        val currency = cart.currency ?: throw BusinessException("Cart currency not defined")
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
