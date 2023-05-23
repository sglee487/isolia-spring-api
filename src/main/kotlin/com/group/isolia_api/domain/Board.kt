package com.group.isolia_api.domain

import jakarta.persistence.*
import java.sql.Date

@Entity
class Board(
    @Enumerated(EnumType.STRING)
    val boardType: BoardType = BoardType.FREE,
    val title: String,
    var content: String,
    var previewText: String,
    var previewImage: String?,
    var hits: Int = 0,
    var likes: Int = 0,
    var dislikes: Int = 0,
    var active: Boolean = true,
    val createdAt: Date = Date(System.currentTimeMillis()),
    var updatedAt: Date = Date(System.currentTimeMillis()),
    var deletedAt: Date? = null,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    val user: User,

    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {
}