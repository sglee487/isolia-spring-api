package com.group.isolia_api.domain

import com.fasterxml.jackson.annotation.JsonValue

enum class LoginType(
    val value: String = "email"
) {
    EMAIL("email"),
    NAVER("naver"),
    GOOGLE("google"),
    APPLE("apple");

    @JsonValue
    fun toJsonValue(): String {
        return value
    }
}