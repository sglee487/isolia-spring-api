package com.group.isolia_api.schemas.comment.response

import com.group.isolia_api.domain.Comment
import java.sql.Date

data class CommentGetResponse(
    val content: String,
    var likes: Int = 0,
    val active: Boolean = true,
    val createdAt: Date,
    val updatedAt: Date,
    val deletedAt: Date?,
    // TODO: user info
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
            )
        }
    }
}