package com.group.isolia_api.repository.user

import com.group.isolia_api.domain.LoginType
import com.group.isolia_api.domain.User
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, Long> {

      fun findByLoginTypeAndEmail(loginType: LoginType, email: String): User?
}