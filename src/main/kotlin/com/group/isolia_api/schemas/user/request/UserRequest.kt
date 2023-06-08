package com.group.isolia_api.schemas.user.request

import com.group.isolia_api.domain.LoginType

class UserCreateRequest(
    val snsSub: String?,
    val loginType: LoginType,
    val email: String,
    val password: String,
    val displayName: String,
    var picture32: String?,
    var picture96: String?,
)

class UserUpdateRequest(
    val loginType: LoginType,
    val email: String,
    val displayName: String?,
    val password: String,
    val newPassword: String?,
    var picture32: String?,
    var picture96: String?,
)

class UserLoginRequest(
    val loginType: LoginType,
    val email: String,
    val password: String,
)

class UserSNSLoginRequest(
    val snsToken: String
)