package com.group.isolia_api.schemas.board.request

import com.group.isolia_api.domain.BoardType

data class BoardPostCreateRequest(
    val boardType: BoardType = BoardType.FREE,
    val title: String = "",
    val content: String = ""
) {

    override fun toString(): String {
        return "BoardPostCreateRequest(boardType=$boardType, title='$title', content='$content')"
    }
}

data class BoardCommentCreateRequest(
    val content: String = ""
) {

    override fun toString(): String {
        return "BoardCommentCreateRequest(content='$content')"
    }
}
