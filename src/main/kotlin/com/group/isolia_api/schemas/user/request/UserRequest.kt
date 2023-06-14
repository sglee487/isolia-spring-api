package com.group.isolia_api.schemas.user.request

import com.group.isolia_api.domain.LoginType
import java.net.URL

class UserCreateRequest(
    val snsSub: String?,
    val loginType: LoginType,
    val email: String,
    val password: String,
    val displayName: String,
    var picture32: URL?,
    var picture96: URL?,
)

class UserUpdateRequest(
    val loginType: LoginType,
    val email: String,
    val displayName: String?,
    val password: String,
    val newPassword: String?,
    var picture32: URL?,
    var picture96: URL?,
)

class UserLoginRequest(
    val loginType: LoginType,
    val email: String,
    val password: String,
)
