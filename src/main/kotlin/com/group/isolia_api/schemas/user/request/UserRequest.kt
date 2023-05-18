package com.group.isolia_api.schemas.user.request

import com.group.isolia_api.domain.LoginType

data class UserCreateRequest(
    val snsSub: String?,
    val loginType: LoginType,
    val email: String,
    val password: String,
    var picture32: String?,
    var picture96: String?,
)