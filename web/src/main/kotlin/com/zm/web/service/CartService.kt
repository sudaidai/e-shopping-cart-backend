package com.zm.web.service;

import com.zm.web.constant.CartAction
import com.zm.web.model.request.CartItemUpdateRequest
import com.zm.web.model.response.CartItemResponse
import com.zm.web.repository.CartItemRepository
import com.zm.web.repository.data.CartItem
import com.zm.web.repository.data.Member
import com.zm.web.repository.data.Product
import com.zm.web.utils.SecurityUtils
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class CartService(
    private val cartItemRepository: CartItemRepository,
    private val memberService: MemberService
) {

    fun listCartItems(page: Int, size: Int): Page<CartItemResponse> {
        return cartItemRepository.findByMember(
            Member(account = SecurityUtils.getCurrentUser()), PageRequest.of(page, size)
        ).map { it.toCartItemResponse() }
    }

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
        val rowsDeleted = cartItemRepository.deleteByMember(Member(id = member.id))
        return rowsDeleted > 0
    }

    fun updateCart(updateCartRequest: List<CartItemUpdateRequest>): List<CartItemResponse> {
        val member = memberService.getCurrentMember()
        val updatedCartItems = mutableListOf<CartItemResponse>()

        for (updateCartItem in updateCartRequest) {
            when (updateCartItem.action) {
                CartAction.ADD -> {
                    val cartItem = updateCartItem.quantity?.let {
                        CartItem(
                            member = Member(id = member.id),
                            product = Product(id = updateCartItem.productId),
                            quantity = it
                        )
                    }
                    cartItem?.let { cartItemRepository.save(it).toCartItemResponse() }?.let { updatedCartItems.add(it) }
                }
                CartAction.UPDATE -> {
                    val cartItem = updateCartItem.cartItemId?.let { cartItemRepository.findById(it) }
                    if (cartItem != null) {
                        if (cartItem.isPresent) {
                            val existingCartItem = cartItem.get()
                            existingCartItem.quantity = updateCartItem.quantity!!
                            updatedCartItems.add(cartItemRepository.save(existingCartItem).toCartItemResponse())
                        }
                    }
                }
                CartAction.REMOVE -> {
                    updateCartItem.cartItemId?.let { cartItemRepository.deleteById(it) }
                }
                CartAction.CLEAR -> {
                    cartItemRepository.deleteByMember(Member(id = member.id))
                }
            }
        }

        return updatedCartItems
    }
}

