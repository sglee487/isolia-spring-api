package com.group.isolia_api.repository.minesweeper

import org.springframework.stereotype.Repository

@Repository
class MineSweeperRepository {
    fun reset() {
        MineFieldMemoryDatabase.reset()
    }
}