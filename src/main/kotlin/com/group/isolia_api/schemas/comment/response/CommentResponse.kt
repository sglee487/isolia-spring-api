package com.group.isolia_api.schemas.comment.response

import com.group.isolia_api.domain.Comment
import com.group.isolia_api.schemas.board.response.BoardUserInfo
import java.time.LocalDateTime

data class CommentGetResponse(
    val content: String,
    var likes: Int = 0,
    val active: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?,
    val userInfo: BoardUserInfo
) {
    companion object {
        fun of(comment: Comment): CommentGetResponse {
            return CommentGetResponse(
                content = comment.content,
                likes = comment.likes,
                active = comment.active,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
                deletedAt = comment.deletedAt,
                userInfo = BoardUserInfo.of(comment.user)
            )
        }
    }
}

data class CommentCreateResponse(
    val content: String,
    var likes: Int = 0,
    val active: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?,
    val userInfo: BoardUserInfo
) {
    companion object {
        fun of(comment: Comment): CommentGetResponse {
            return CommentGetResponse(
                content = comment.content,
                likes = comment.likes,
                active = comment.active,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
                deletedAt = comment.deletedAt,
                userInfo = BoardUserInfo.of(comment.user)
            )
        }
    }
}