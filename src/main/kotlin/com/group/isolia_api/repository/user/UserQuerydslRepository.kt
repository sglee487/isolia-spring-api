package com.group.isolia_api.repository.user

import com.group.isolia_api.domain.LoginType
import com.group.isolia_api.domain.QUser.user
import com.group.isolia_api.domain.User
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component


@Component
class UserQuerydslRepository(
      private val queryFactory: JPAQueryFactory
) {
      fun findByLoginTypeAndEmail(loginType: LoginType, email: String): User? {
            return queryFactory.select(user)
                  .from(user)
                  .where(
                        user.loginType.eq(loginType),
                        user.email.eq(email)
                  )
                  .limit(1)
                  .fetchOne()
      }
}