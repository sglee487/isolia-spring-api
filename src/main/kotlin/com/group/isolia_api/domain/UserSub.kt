package com.group.isolia_api.domain

import kotlinx.serialization.Serializable

@Serializable
data class UserSub(
    val id: Long,
    val loginType: LoginType,
    val email: String
) {
    override fun toString(): String {
        return "UserSub(id=$id, loginType=$loginType, email='$email')"
    }
}