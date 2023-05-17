package com.group.isolia_api.domain

enum class LoginType(
    val value: String = "email"
) {
    EMAIL("email"),
    NAVER("naver"),
    GOOGLE("google"),
    APPLE("apple")
}