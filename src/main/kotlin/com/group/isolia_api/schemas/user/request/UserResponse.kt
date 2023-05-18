package com.group.isolia_api.schemas.user.request

import com.group.isolia_api.domain.LoginType
import com.group.isolia_api.domain.User

class UserCreateResponse(
    private val user: User
) {
    val id: Long = user.id!!
    val loginType: LoginType = user.loginType
    val email: String = user.email
    val displayName: String = user.displayName
    var picture32: String? = user.picture32
    var picture96: String? = user.picture96
}

class UserUpdateResponse(
    private val user: User
) {
    val id: Long = user.id!!
    val loginType: LoginType = user.loginType
    val email: String = user.email
    val displayName: String = user.displayName
    var picture32: String? = user.picture32
    var picture96: String? = user.picture96
}

class UserLoginResponse(
    private val user: User
) {
    val id: Long = user.id!!
    val loginType: LoginType = user.loginType
    val email: String = user.email
    val displayName: String = user.displayName
    var picture32: String? = user.picture32
    var picture96: String? = user.picture96
}