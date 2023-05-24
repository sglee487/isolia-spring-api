package com.group.isolia_api.repository.comment

import com.group.isolia_api.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository


interface CommentRepository : JpaRepository<Comment, Long> {

}
