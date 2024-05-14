package com.zm.web.controller

import com.zm.web.model.fromProduct
import com.zm.web.model.response.HomeResponse
import com.zm.web.repository.ItemRepository
import com.zm.web.repository.ProductRepository
import com.zm.web.repository.data.Cart
import com.zm.web.service.MemberService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/home")
class HomeController(
    private val memberService: MemberService,
    private val productRepository: ProductRepository,
    private val itemRepository: ItemRepository
) {

    @GetMapping
    fun home(request: HttpServletRequest): ResponseEntity<HomeResponse> {
        val baseUrl = "${request.scheme}://${request.serverName}:${request.serverPort}/"
        val member = memberService.getCurrentMember()
        val products = productRepository.findAll().map { fromProduct(it, baseUrl) }
        val cartId = member?.cart?.id
        val itemsCount = cartId?.let { itemRepository.countByCart(Cart(id = it)) } ?: 0
        val memberName = member?.name ?: "Guest"
        val isCartEmpty = itemsCount == 0

        return ResponseEntity.ok(
            HomeResponse(
                name = memberName,
                products = products,
                cartId = cartId,
                itemsCount = itemsCount,
                isCartEmpty = isCartEmpty
            )
        )
    }
}