package com.group.isolia_api.domain

import java.net.URL
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Board(
    @Enumerated(EnumType.STRING)
    val boardType: BoardType = BoardType.FREE,
    val title: String,
    @Column(columnDefinition = "TEXT")
    var content: String,
    var previewText: String,
    var previewImage: URL?,
    var hits: Int = 0,
    var likes: Int = 0,
    var dislikes: Int = 0,
    var active: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    val user: User,

    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)