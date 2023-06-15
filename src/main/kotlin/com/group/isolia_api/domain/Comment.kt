package com.group.isolia_api.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Comment(
    var content: String,
    var likes: Int = 0,
    var active: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    val user: User,

    @ManyToOne
    val board: Board,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)