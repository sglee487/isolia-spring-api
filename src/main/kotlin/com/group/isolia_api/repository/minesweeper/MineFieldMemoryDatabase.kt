package com.group.isolia_api.repository.minesweeper

import com.group.isolia_api.controller.ActionType
import kotlinx.serialization.Serializable

@Serializable
data class CustomCoordinate(
    val x: Int,
    val y: Int,
)

@Serializable
data class ActionHistory(
    val action: ActionType,
    val name: String,
    val color: String,
    val x: Int,
    val y: Int,
)

object MineFieldMemoryDatabase {
    internal const val size: Int = 12
    internal const val mines: Int = 20
    internal val mineCoords = mutableListOf<CustomCoordinate>()
    internal val actionHistory = mutableListOf<ActionHistory>()

    fun reset() {
        mineCoords.clear()
        actionHistory.clear()
        for (i in 0 until mines) {
            var x = (Math.random() * size).toInt()
            var y = (Math.random() * size).toInt()
            while (mineCoords.contains(CustomCoordinate(x, y))) {
                x = (Math.random() * size).toInt()
                y = (Math.random() * size).toInt()
            }
            mineCoords.add(CustomCoordinate(x, y))
        }
    }

    fun addHistory(actionType: ActionType, name: String, color: String, x: Int, y: Int) {
        actionHistory.add(ActionHistory(
            actionType,
            name,
            color,
            x,
            y
        ))
    }
}