package com.group.isolia_api.domain.MineSweeper

import kotlinx.serialization.Serializable

@Serializable
data class MineSweeperPlayer(
    val sid: String,
    val name: String,
    val color: String,
)
