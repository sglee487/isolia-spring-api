package com.group.isolia_api.repository.board

import com.group.isolia_api.domain.Board
import com.group.isolia_api.domain.BoardType
import org.springframework.data.jpa.repository.JpaRepository


interface BoardRepository : JpaRepository<Board, Long> {
    fun findAllByActiveTrue(): List<Board>

    fun findAllByBoardTypeEqualsAndActiveTrue(boardType: BoardType): List<Board>
}
