package com.group.isolia_api.repository.board

import com.group.isolia_api.domain.Board
import org.springframework.data.jpa.repository.JpaRepository


interface BoardRepository : JpaRepository<Board, Long> {

}