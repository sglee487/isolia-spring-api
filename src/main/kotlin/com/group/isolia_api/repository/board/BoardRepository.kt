package com.group.isolia_api.repository.board

import com.group.isolia_api.domain.Board
import com.group.isolia_api.domain.BoardType
import org.springframework.data.jpa.repository.JpaRepository


interface BoardRepository : JpaRepository<Board, Long> {

    fun getByIdAndActiveIsTrue(id: Long): Board?

    fun findAllByActiveIsTrue(): List<Board>

    fun findAllByBoardTypeEqualsAndActiveIsTrue(boardType: BoardType): List<Board>
}
