package com.group.isolia_api.domain.MineSweeper

import com.fasterxml.jackson.annotation.JsonValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ActionType(
    val value: String,
) {
    @SerialName("reveal")
    REVEAL("reveal"),

    @SerialName("flag")
    FLAG("flag");

    @JsonValue
    fun toJsonValue(): String {
        return value
    }
}