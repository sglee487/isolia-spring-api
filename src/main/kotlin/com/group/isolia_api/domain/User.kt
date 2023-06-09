package com.group.isolia_api.domain

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.net.URL
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.persistence.*


private val encoder = BCryptPasswordEncoder()

@Entity(name = "isolia_user")
@Table(
    uniqueConstraints = [UniqueConstraint(
        name = "user_loginType_email_unique",
        columnNames = ["loginType", "email"]
    )]
)

data class User(
    val snsSub: String?,
    @Enumerated(EnumType.STRING)
    val loginType: LoginType = LoginType.EMAIL,
    val email: String,
    var password: String?,
    var displayName: String,
    var picture32: URL?,
    var picture96: URL?,
    var active: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
    var updatedAt: LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
    var deletedAt: LocalDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {

    fun getUserSub(): UserSub = UserSub(
        id ?: run { throw IllegalArgumentException("Not user id exist. May be not registered.") },
        loginType,
        email
    )

    fun updateUser(
        displayName: String?,
        password: String,
        newPassword: String?,
        picture32: URL?,
        picture96: URL?,
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