package com.group.isolia_api.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class User (
    val snsSub: String?,
    val loginType: LoginType = LoginType.EMAIL,
    val email: String,
    var picture32: String?,
    var picture96: String?,
    var password: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {
}