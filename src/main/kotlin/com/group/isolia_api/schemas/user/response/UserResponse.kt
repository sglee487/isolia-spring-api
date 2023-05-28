package com.group.isolia_api.schemas.user.response

import com.group.isolia_api.domain.LoginType
import com.group.isolia_api.domain.User

class UserCreateResponse(
    private val user: User,
    private val _jwt: String,
    private val _exp: Long?
) {
    val id: Long = user.id!!
    val loginType: LoginType = user.loginType
    val email: String = user.email
    val displayName: String = user.displayName
    var picture32: String? = user.picture32
    var picture96: String? = user.picture96
    val jwt: String = _jwt
    val exp: Long? = _exp

    override fun toString(): String {
        return "UserCreateResponse(id=$id, loginType=$loginType, email='$email', displayName='$displayName', picture32=$picture32, picture96=$picture96)"
    }
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

    override fun toString(): String {
        return "UserUpdateResponse(id=$id, loginType=$loginType, email='$email', displayName='$displayName', picture32=$picture32, picture96=$picture96)"
    }
}

class UserLoginResponse(
    private val user: User,
    private val _jwt: String,
    private val _exp: Long?
) {
    val id: Long = user.id!!
    val loginType: LoginType = user.loginType
    val email: String = user.email
    val displayName: String = user.displayName
    var picture32: String? = user.picture32
    var picture96: String? = user.picture96
    val jwt: String = _jwt
    val exp: Long? = _exp


    override fun toString(): String {
        return "UserLoginResponse(id=$id, loginType=$loginType, email='$email', displayName='$displayName', picture32=$picture32, picture96=$picture96)"
    }
}