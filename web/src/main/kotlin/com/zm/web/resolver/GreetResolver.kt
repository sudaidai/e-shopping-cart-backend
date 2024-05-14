package com.zm.web.resolver

import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class GreetResolver {

    @QueryMapping
    fun hello(): String = "Hello, World!"

}