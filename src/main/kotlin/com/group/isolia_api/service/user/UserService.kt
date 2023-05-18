package com.group.isolia_api.service.user

import com.group.isolia_api.domain.User
//import com.group.isolia_api.repository.user.UserRepository
import com.group.isolia_api.schemas.user.request.UserCreateRequest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
//    private val userRepository: UserRepository
) {
    private val encoder = BCryptPasswordEncoder()
    fun registerUser(request: UserCreateRequest) {
        val newUser = User(
            snsSub = request.snsSub,
            loginType = request.loginType,
            email = request.email,
            password = encoder.encode(request.password),
            picture32 = request.picture32,
            picture96 = request.picture96,
        )
        println(newUser)
//        userRepository.save(newUser)
    }
}