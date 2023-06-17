package com.group.isolia_api.repository.board

import com.group.isolia_api.domain.Board
import com.group.isolia_api.domain.BoardType
import com.group.isolia_api.domain.QBoard.board
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component


@Component
class BoardQuerydslRepository(
    private val queryFactory: JPAQueryFactory
) {
    fun getBoardList(boardType: BoardType, page: Int, pageSize: Int = 10): PageImpl<Board> {
        val query = queryFactory.selectFrom(board)
            .where(
                if (boardType != BoardType.ALL) board.boardType.eq(boardType) else null,
                board.active.isTrue
            )
            .orderBy(board.id.desc())

        val totalCount = query.fetch().size.toLong()
        val pageable: Pageable = PageRequest.of(page - 1, pageSize)
        val boardList = query.offset(pageable.offset).limit(pageable.pageSize.toLong()).fetch()

        return PageImpl(boardList, pageable, totalCount)
    }
}