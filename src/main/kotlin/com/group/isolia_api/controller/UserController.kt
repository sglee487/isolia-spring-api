package com.group.isolia_api.controller

import com.group.isolia_api.schemas.user.request.UserCreateRequest
import com.group.isolia_api.service.user.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/user")
    fun registerUser(@RequestBody request: UserCreateRequest) {
        userService.registerUser(request)
    }
}