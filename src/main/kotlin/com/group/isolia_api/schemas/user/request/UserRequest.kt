package com.group.isolia_api.schemas.user.request

import com.group.isolia_api.domain.LoginType

data class UserCreateRequest(
    val loginType: LoginType,
    val email: String
)