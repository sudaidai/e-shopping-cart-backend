package com.zm.web.service;

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
}

