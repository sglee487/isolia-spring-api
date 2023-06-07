package com.group.isolia_api.repository.minesweeper

import com.group.isolia_api.domain.MineSweeper.ActionType
import com.group.isolia_api.schemas.MineSweeper.response.MineSweeperRepositoryResponse
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
