package com.group.isolia_api.repository.board

import com.group.isolia_api.domain.Board
import com.group.isolia_api.domain.BoardType
import com.group.isolia_api.domain.QBoard.board
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component


@Component
class BoardQuerydslRepository(
      private val queryFactory: JPAQueryFactory
) {
      fun getBoardList(boardType: BoardType?): List<Board> {
            return queryFactory.select(board)
                  .from(board)
                  .where(
                        boardType?.let { board.boardType.eq(boardType) },
                        board.active.isTrue
                  ).fetch()
      }
}