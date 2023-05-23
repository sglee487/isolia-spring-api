package com.group.isolia_api.repository.board

import com.group.isolia_api.domain.Board
import org.springframework.data.jpa.repository.JpaRepository


interface BoardRepository : JpaRepository<Board, Long> {
//    fun findAllAndActive(): List<Board> {
//        return findAll()
//    }
//    fun findAllByBoardTypeAndActive(boardType: BoardType): List<Board> {
//        return findAll()
//    }
}

class BoardRepositoryCalss : BoardRepository {

}