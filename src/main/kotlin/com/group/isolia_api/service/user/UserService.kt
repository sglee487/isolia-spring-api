package com.group.isolia_api.service.user

import com.group.isolia_api.domain.User
import com.group.isolia_api.repository.user.UserQuerydslRepository
import com.group.isolia_api.repository.user.UserRepository
import com.group.isolia_api.schemas.user.request.UserCreateRequest
import com.group.isolia_api.schemas.user.request.UserLoginRequest
import com.group.isolia_api.schemas.user.request.UserUpdateRequest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userQuerydslRepository: UserQuerydslRepository,
) {
    private val encoder = BCryptPasswordEncoder()

    @Transactional
    fun registerUser(request: UserCreateRequest): User {
        val newUser = User(
            snsSub = request.snsSub,
            loginType = request.loginType,
            email = request.email,
            password = encoder.encode(request.password),
            displayName = request.displayName,
            picture32 = request.picture32,
            picture96 = request.picture96,
        )
        userRepository.save(newUser)

        return newUser
    }

    @Transactional
    fun updateUser(request: UserUpdateRequest): User {
        val user = userQuerydslRepository.findByLoginTypeAndEmail(request.loginType, request.email)
            ?: throw IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        user.updateUser(
            displayName = request.displayName,
            password = request.password,
            newPassword = request.newPassword,
            picture32 = request.picture32,
            picture96 = request.picture96,
        )

        return user
    }

    @Transactional
    fun loginUser(request: UserLoginRequest): User {
        val user = userQuerydslRepository.findByLoginTypeAndEmail(request.loginType, request.email)
            ?: throw IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        if (!encoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("비밀번호가 틀렸습니다.")
        }
        return user
    }

    @Transactional
    fun isRegisteredUser(request: UserLoginRequest): Boolean {
        userQuerydslRepository.findByLoginTypeAndEmail(request.loginType, request.email)
            ?: return false
        return true
    }
}