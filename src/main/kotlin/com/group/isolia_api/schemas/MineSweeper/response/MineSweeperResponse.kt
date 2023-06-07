package com.group.isolia_api.schemas.MineSweeper.response

import com.group.isolia_api.domain.MineSweeper.ActionType
import com.group.isolia_api.repository.minesweeper.ActionHistory
import com.group.isolia_api.repository.minesweeper.CustomCoordinate
import com.group.isolia_api.repository.minesweeper.MineFieldMemoryDatabase
import kotlinx.serialization.Serializable

@Serializable
class MineSweeperRepositoryResponse(
    val size: Int,
    val mines: Int,
    val bombCoords: List<CustomCoordinate>,
    val actionHistory: List<ActionHistory>,
){
    companion object {
        fun of(
            mineFieldMemoryDatabase: MineFieldMemoryDatabase
        ): MineSweeperRepositoryResponse {
            return MineSweeperRepositoryResponse(
                size = mineFieldMemoryDatabase.size,
                mines = mineFieldMemoryDatabase.mines,
                bombCoords = mineFieldMemoryDatabase.mineCoords,
                actionHistory = mineFieldMemoryDatabase.actionHistory,
            )
        }
    }

    override fun toString(): String {
        return "RepositoryResponse(size=$size, mines=$mines, mineCoords=$bombCoords, actionHistory=$actionHistory)"
    }
}

@Serializable
class ActionResponse(
    val action: ActionType,
    val x: Int,
    val y: Int,
    val color: String,
    val history: List<ActionHistory>,
)