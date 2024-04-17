package com.zm.web.service;

import com.zm.web.exception.BusinessException
import com.zm.web.model.response.CartItemResponse
import com.zm.web.repository.CartItemRepository
import com.zm.web.repository.MemberRepository
import com.zm.web.repository.ProductRepository
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
    private val memberRepository: MemberRepository,
    private val productRepository: ProductRepository
) {

    fun listCartItems(page: Int, size: Int): Page<CartItemResponse> {
        return cartItemRepository.findByMember(
            Member(account = SecurityUtils.getCurrentUser()), PageRequest.of(page, size)
        ).map { it.toCartItemResponse() }
    }

    @Transactional
    fun addToCart(productId: UUID, quantity: Int): CartItemResponse {
        val currentAccount = SecurityUtils.getCurrentUser()
        val member = memberRepository.findByAccount(account = currentAccount)
            ?: throw BusinessException("Member not found for account: $currentAccount")
        val cartItem = CartItem(
            member = Member(id = member.id),
            product = Product(id = productId),
            quantity = quantity
        )

        return cartItemRepository.save(cartItem).toCartItemResponse()
    }

    @Transactional
    fun increaseCartItemQuantity(productId: UUID, quantity: Int): CartItemResponse {
        val currentAccount = SecurityUtils.getCurrentUser()
        val member = memberRepository.findByAccount(account = currentAccount)
            ?: throw BusinessException("Member not found for account: $currentAccount")

        val product = productRepository.findById(productId)
            .orElseThrow { BusinessException("Product not found with ID: $productId") }

        val cartItem = cartItemRepository.findByMemberAndProduct(member, product)
            ?: throw BusinessException("CartItem not found for member: ${member.id} and product: ${product.id}")
        cartItem.addQuantity(quantity)
        return cartItemRepository.save(cartItem).toCartItemResponse()
    }
}

