package com.zm.web.controller

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.zm.web.model.ProductDTO
import com.zm.web.model.fromProduct
import com.zm.web.repository.ItemRepository
import com.zm.web.repository.ProductRepository
import com.zm.web.repository.data.Cart
import com.zm.web.serializer.CustomSerializer
import com.zm.web.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/home")
class HomeController (
    private val memberService: MemberService,
    private val productRepository: ProductRepository,
    private val itemRepository: ItemRepository
){

    @GetMapping
    fun home(): ResponseEntity<HomeResponse> {
        return try {
            val member = memberService.getCurrentMember()
            val products = productRepository.findAll().map { fromProduct(it) }
            val cartId = member?.cart?.uuid
            val itemsCount = member?.cart?.id?.let { itemRepository.findByCart(Cart(id = it)) } ?: 0
            val memberName = member?.name ?: "Guest"
            val isCartEmpty = itemsCount == 0

            ResponseEntity.ok(HomeResponse(
                name = memberName,
                products = products,
                cartId = cartId,
                itemsCount = itemsCount,
                isCartEmpty = isCartEmpty
            ))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }
}

@JsonSerialize(using = CustomSerializer::class)
data class HomeResponse(
    val name: String,
    val products: List<ProductDTO>,
    val cartId: UUID?,
    val itemsCount: Int,
    val isCartEmpty: Boolean
)
