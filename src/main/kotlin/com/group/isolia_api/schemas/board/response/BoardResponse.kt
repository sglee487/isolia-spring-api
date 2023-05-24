package com.group.isolia_api.schemas.board.response

import com.group.isolia_api.domain.Board
import com.group.isolia_api.domain.User
import com.group.isolia_api.schemas.comment.response.CommentGetResponse
import java.sql.Date


data class BoardUserInfo(
    val id: Long,
    val email: String,
    val displayName: String,
    val picture32: String?,
    val picture96: String?
) {
    companion object {
        fun of(user: User): BoardUserInfo {
            return BoardUserInfo(
                id = user.id!!,
                email = user.email,
                displayName = user.displayName,
                picture32 = user.picture32,
                picture96 = user.picture96,
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
    val comments: List<CommentGetResponse>
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
                comments = board.comments.map { CommentGetResponse.of(it) }
            )
        }
    }
}

data class BoardPostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val hits: Int = 0,
    val likes: Int = 0,
    val dislikes: Int = 0,
    val createdAt: Date,
    val updatedAt: Date,
    val deletedAt: Date?,
    val userInfo: BoardUserInfo,
    val comments: List<CommentGetResponse>
) {
    companion object {
        fun of(board: Board, user: User): BoardPostResponse {
            return BoardPostResponse(
                id = board.id!!,
                title = board.title,
                content = board.content,
                hits = board.hits,
                likes = board.likes,
                dislikes = board.dislikes,
                createdAt = board.createdAt,
                updatedAt = board.updatedAt,
                deletedAt = board.deletedAt,
                userInfo = BoardUserInfo.of(user),
                comments = board.comments.map { CommentGetResponse.of(it) }
            )
        }
    }
}