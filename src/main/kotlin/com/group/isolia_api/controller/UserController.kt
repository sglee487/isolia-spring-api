package com.group.isolia_api.controller

import com.group.isolia_api.schemas.user.request.UserCreateRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @PostMapping("/user")
    fun registerUser(@RequestBody request: UserCreateRequest) {
        bookService.saveBook(request)
    }
}