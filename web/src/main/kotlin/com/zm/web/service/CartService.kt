package com.zm.web.service

import com.zm.web.constant.CartAction
import com.zm.web.model.request.CartItemUpdateRequest
import com.zm.web.model.response.CartItemResponse
import com.zm.web.repository.CartItemRepository
import com.zm.web.repository.data.CartItem
import com.zm.web.repository.data.Member
import com.zm.web.repository.data.Product
import com.zm.web.utils.SecurityUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class CartService(
    private val cartItemRepository: CartItemRepository,
    private val memberService: MemberService
) {

    fun listCartItems(page: Int, size: Int): Page<CartItemResponse> =
        cartItemRepository.findByMember(
            Member(account = SecurityUtils.getCurrentUser()), PageRequest.of(page, size)
        ).map { it.toCartItemResponse() }

    fun addToCart(productId: UUID, quantity: Int): CartItemResponse {
        val member = memberService.getCurrentMember()
        val cartItem = CartItem(
            member = Member(id = member.id),
            product = Product(id = productId),
            quantity = quantity
        )
        return cartItemRepository.save(cartItem).toCartItemResponse()
    }

    fun clearCart(): Boolean {
        val member = memberService.getCurrentMember()
        return cartItemRepository.deleteByMember(Member(id = member.id)) > 0
    }

    fun updateCart(updateCartRequest: List<CartItemUpdateRequest>): List<CartItemResponse> {
        val member = memberService.getCurrentMember()

        return updateCartRequest.mapNotNull { updateCartItem ->
            when (updateCartItem.action) {
                CartAction.ADD -> updateCartItem.quantity?.let {
                    val cartItem = CartItem(
                        member = Member(id = member.id),
                        product = Product(id = updateCartItem.productId),
                        quantity = it
                    )
                    cartItemRepository.save(cartItem).toCartItemResponse()
                }
                CartAction.UPDATE -> {
                    updateCartItem.cartItemId?.let { cartItemId ->
                        cartItemRepository.findById(cartItemId).orElse(null)?.let { existingCartItem ->
                            existingCartItem.quantity = updateCartItem.quantity!!
                            cartItemRepository.save(existingCartItem).toCartItemResponse()
                        }
                    }
                }
                CartAction.REMOVE -> {
                    updateCartItem.cartItemId?.let { cartItemRepository.deleteById(it) }
                    null
                }
                CartAction.CLEAR -> {
                    cartItemRepository.deleteByMember(Member(id = member.id))
                    null
                }
            }
        }
    }
}
