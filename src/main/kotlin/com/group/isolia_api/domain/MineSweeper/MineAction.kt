package com.group.isolia_api.domain.MineSweeper

import kotlinx.serialization.Serializable

@Serializable
data class MineAction(
    val sid: String,
    val actionType: ActionType,
    val x: Int,
    val y: Int,
)
