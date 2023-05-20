package com.group.isolia_api.domain

import jakarta.persistence.*
import java.sql.Date

@Entity
class Comment(
        var content: String,
        var likes: Int = 0,
        var isDeleted: Boolean = false,
        val createdAt: Date = Date(System.currentTimeMillis()),
        var updatedAt: Date = Date(System.currentTimeMillis()),
        var deletedAt: Date? = null,

        @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
        val user: User,

        @ManyToOne
        val board: Board,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null
) {
}