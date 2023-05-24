package com.group.isolia_api.schemas.comment.request

data class CommentCreateRequest(
    val content: String = ""
) {

    override fun toString(): String {
        return "CommentCreateRequest(content='$content')"
    }
}
