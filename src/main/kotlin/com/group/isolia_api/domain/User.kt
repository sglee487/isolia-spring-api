package com.group.isolia_api.domain

import jakarta.persistence.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


private val encoder = BCryptPasswordEncoder()

@Entity(name = "isolia_user")
@Table(
    uniqueConstraints = [UniqueConstraint(
        name = "user_loginType_email_unique",
        columnNames = ["loginType", "email"]
    )]
)

class User(
    val snsSub: String?,
    @Enumerated(EnumType.STRING)
    val loginType: LoginType = LoginType.EMAIL,
    val email: String,
    var password: String,
    var displayName: String,
    var picture32: String?,
    var picture96: String?,
    var active: Boolean = true,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {

    fun getJwtSub(): String = Json.encodeToString(UserSub(id!!, loginType, email))

    fun updateUser(
        displayName: String?,
        password: String,
        newPassword: String?,
        picture32: String?,
        picture96: String?,
    ) {
        if (!encoder.matches(password, this.password)) {
            throw IllegalArgumentException("비밀번호가 틀렸습니다.")
        }
        if (displayName != null) {
            this.displayName = displayName
        }
        if (newPassword != null) {
            if (encoder.matches(password, this.password)) {
                this.password = encoder.encode(newPassword)
            } else {
                throw IllegalArgumentException("비밀번호가 일치하지 않습니다")
            }
        }
        if (picture32 != null) {
            this.picture32 = picture32
        }
        if (picture96 != null) {
            this.picture96 = picture96
        }
    }
}