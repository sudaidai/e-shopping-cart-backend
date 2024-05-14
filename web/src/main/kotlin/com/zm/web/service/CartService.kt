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

    fun queryCart(id: String): CartResponse =
        cartRepository.findById(id.toLong())
            .orElseThrow { NoSuchElementException("Cart with ID $id not found") }
            .let { prepareCartResponse(it) }

    fun addItemToCart(productId: String, quantity: Int): CartResponse =
        getCurrentMember()
            .let { member ->
                cartRepository.findByMember(member) ?: cartRepository.save(Cart(member = member))
            }
            .let { cart ->
                cart.cartItems.add(Item(cart = cart, member = cart.member, product = Product(productId.toLong()), quantity = quantity))
                cartRepository.save(cart)
            }
            .let { prepareCartResponse(it) }

    fun updateCartItem(cartItemId: String, quantity: Int): CartResponse =
        getCurrentMember()
            .let { member ->
                cartRepository.findByMember(member) ?: throw BusinessException("No cart exist. Unable to update cart")
            }
            .let { cart ->
                cart.cartItems.find { it.id == cartItemId.toLong() }
                    ?.apply { this.quantity += quantity }
                    ?: throw BusinessException("Item not found in cart")
                cartRepository.save(cart)
            }
            .let { prepareCartResponse(it) }

    fun removeCartItem(cartItemId: String): CartResponse =
        getCurrentMember()
            .let { member ->
                cartRepository.findByMember(member) ?: throw BusinessException("No cart exist. Unable to delete item from cart")
            }
            .let { cart ->
                cart.cartItems.find { it.id == cartItemId.toLong() }
                    ?.let { item ->
                        cart.cartItems.remove(item)
                        cartRepository.save(cart)
                    }
                    ?: throw BusinessException("Item not found in cart")
            }
            .let { prepareCartResponse(it) }

    fun clearCart(): CartResponse =
        getCurrentMember()
            .let { member ->
                cartRepository.findByMember(member) ?: throw BusinessException("No cart exist.")
            }
            .let { cart ->
                cart.cartItems.clear()
                cartRepository.save(cart)
            }
            .let { prepareCartResponse(it) }

    private fun prepareCartResponse(cart: Cart): CartResponse {
        val currency = cart.currency ?: throw BusinessException("Cart currency not defined")
        val subTotal = calculateSubTotal(cart.cartItems)
        val shippingTotal = calculateShippingTotal(cart.cartItems)
        val taxTotal = calculateTaxTotal(subTotal)
        val grandTotal = subTotal + shippingTotal + taxTotal

        return cart.toCartResponse(currency, subTotal, shippingTotal, taxTotal, grandTotal)
    }

    private fun calculateSubTotal(items: List<Item>): BigDecimal =
        items.sumOf { item ->
            val price = item.product?.price ?: BigDecimal.ZERO
            price.multiply(BigDecimal(item.quantity))
        }

    private fun calculateShippingTotal(items: List<Item>): BigDecimal =
        BigDecimal(items.size)

    private fun calculateTaxTotal(subTotal: BigDecimal): BigDecimal =
        subTotal.multiply(BigDecimal(0.1))

    private fun getCurrentMember() = memberService.getCurrentMember()
        ?: throw BusinessException("Unauthorized")
}
