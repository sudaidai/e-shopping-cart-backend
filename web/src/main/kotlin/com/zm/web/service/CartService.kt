package com.zm.web.service

import com.zm.web.constant.CartAction
import com.zm.web.model.request.CartItemUpdateRequest
import com.zm.web.model.response.CartItemResponse
import com.zm.web.repository.ItemRepository
import com.zm.web.repository.data.Item
import com.zm.web.repository.data.Member
import com.zm.web.repository.data.Product
import com.zm.web.utils.SecurityUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class CartService(
    private val itemRepository: ItemRepository,
    private val memberService: MemberService
) {

    fun listCartItems(page: Int, size: Int): Page<CartItemResponse> =
        itemRepository.findByMember(
            Member(account = SecurityUtils.getCurrentUser()), PageRequest.of(page, size)
        ).map { it.toCartItemResponse() }

    fun addToCart(productId: UUID, quantity: Int): CartItemResponse {
        val member = memberService.getCurrentMember()
        val item = Item(
            member = Member(id = member.id),
            product = Product(id = productId),
            quantity = quantity
        )
        return itemRepository.save(item).toCartItemResponse()
    }

    fun clearCart(): Boolean {
        val member = memberService.getCurrentMember()
        return itemRepository.deleteByMember(Member(id = member.id)) > 0
    }

    fun updateCart(updateCartRequest: List<CartItemUpdateRequest>): List<CartItemResponse> {
        val member = memberService.getCurrentMember()

        return updateCartRequest.mapNotNull { updateCartItem ->
            when (updateCartItem.action) {
                CartAction.ADD -> updateCartItem.quantity?.let {
                    val item = Item(
                        member = Member(id = member.id),
                        product = Product(id = updateCartItem.productId),
                        quantity = it
                    )
                    itemRepository.save(item).toCartItemResponse()
                }
                CartAction.UPDATE -> {
                    updateCartItem.cartItemId?.let { cartItemId ->
                        itemRepository.findById(cartItemId).orElse(null)?.let { existingCartItem ->
                            existingCartItem.quantity = updateCartItem.quantity!!
                            itemRepository.save(existingCartItem).toCartItemResponse()
                        }
                    }
                }
                CartAction.REMOVE -> {
                    updateCartItem.cartItemId?.let { itemRepository.deleteById(it) }
                    null
                }
                CartAction.CLEAR -> {
                    itemRepository.deleteByMember(Member(id = member.id))
                    null
                }
            }
        }
    }
}
