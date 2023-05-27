package com.group.isolia_api.domain

import java.sql.Date
import javax.persistence.*

@Entity
class Comment(
    var content: String,
    var likes: Int = 0,
    var active: Boolean = true,
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