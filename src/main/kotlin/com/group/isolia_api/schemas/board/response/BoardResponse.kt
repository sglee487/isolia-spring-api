package com.group.isolia_api.schemas.board.response

import com.group.isolia_api.domain.Board
import com.group.isolia_api.domain.Comment
import com.group.isolia_api.domain.User
import java.sql.Date


data class BoardUserInfo(
    val id: Long,
    val email: String,
    val displayName: String,
    val picture32: String?,
) {
    companion object {
        fun of(user: User): BoardUserInfo {
            return BoardUserInfo(
                id = user.id!!,
                email = user.email,
                displayName = user.displayName,
                picture32 = user.picture32
            )
        }
    }
}

data class BoardGetResponse(
    val id: Long,
    val boardType: String,
    val title: String,
    val previewText: String,
    val previewImage: String?,
    val hits: Int,
    val likes: Int,
    val dislikes: Int,
    val createdAt: Date,
    val updatedAt: Date,
    val deletedAt: Date?,
    val boardUserInfo: BoardUserInfo,
    val comments: List<Comment>
) {
    companion object {
        fun of(board: Board, user: User): BoardGetResponse {
            return BoardGetResponse(
                id = board.id!!,
                boardType = board.boardType.name,
                title = board.title,
                previewText = board.previewText,
                previewImage = board.previewImage,
                hits = board.hits,
                likes = board.likes,
                dislikes = board.dislikes,
                createdAt = board.createdAt,
                updatedAt = board.updatedAt,
                deletedAt = board.deletedAt,
                boardUserInfo = BoardUserInfo.of(user),
                comments = board.comments
            )
        }
    }
}
