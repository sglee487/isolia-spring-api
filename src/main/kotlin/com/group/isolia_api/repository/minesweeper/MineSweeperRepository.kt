package com.group.isolia_api.repository.minesweeper

import com.group.isolia_api.controller.ActionType
import kotlinx.serialization.Serializable
import org.springframework.stereotype.Repository

@Repository
class MineSweeperRepository {

    init {
        MineFieldMemoryDatabase.reset()
    }
    fun reset() {
        MineFieldMemoryDatabase.reset()
    }

    fun getGameStatus(): MineSweeperRepositoryResponse {
        return MineSweeperRepositoryResponse.of(MineFieldMemoryDatabase)
    }

    fun addHistory(actionType: ActionType, name: String, color: String, x:Int, y:Int) {
        MineFieldMemoryDatabase.addHistory(
            actionType,
            name,
            color,
            x,
            y,
        )
    }

    fun getHistory(): List<ActionHistory> {
        return MineFieldMemoryDatabase.actionHistory
    }
}


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