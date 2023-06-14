package com.group.isolia_api.service.user

import com.fasterxml.jackson.databind.JsonNode
import com.group.isolia_api.domain.LoginType
import com.group.isolia_api.domain.User
import com.group.isolia_api.repository.user.UserQuerydslRepository
import com.group.isolia_api.repository.user.UserRepository
import com.group.isolia_api.schemas.user.request.UserCreateRequest
import com.group.isolia_api.schemas.user.request.UserLoginRequest
import com.group.isolia_api.schemas.user.request.UserUpdateRequest
import com.group.isolia_api.service.S3Service
import org.springframework.core.env.Environment
import org.springframework.http.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.awt.Image
import java.awt.image.BufferedImage
import java.net.URI
import java.net.URL
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO


@Service
class UserService(
    private val userRepository: UserRepository,
    private val userQuerydslRepository: UserQuerydslRepository,
    private val env: Environment,
    private val s3Service: S3Service
) {
    private val encoder = BCryptPasswordEncoder()
    private val restTemplate = RestTemplate()

    fun resizeImage(image: BufferedImage, width: Int, height: Int): BufferedImage {
        val resizedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics = resizedImage.createGraphics()
        graphics.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null)
        graphics.dispose()
        return resizedImage
    }

    fun generateProfileUrls(pictureUrl: URL): List<URL> {
        val pictureName = UUID.randomUUID().toString()
        val originalImage = ImageIO.read(pictureUrl)

        val profileUrls = intArrayOf(32, 96).map { size ->
            val resizedImage = resizeImage(originalImage, size, size)
            val uploadPath = "profile_images/${size}/${pictureName}.jpg"
            val file = Paths.get("temp","${pictureName}_${size}.jpg").toFile()
            ImageIO.write(resizedImage, "jpg", file)
            s3Service.uploadToS3(uploadPath, file)
            file.delete()

            s3Service.getUrlFromS3(uploadPath)

        }
        return profileUrls
    }

    @Transactional
    fun registerUser(request: UserCreateRequest): User {
        val newUser = User(
            snsSub = request.snsSub,
            loginType = request.loginType,
            email = request.email,
            password = encoder.encode(request.password),
            displayName = request.displayName,
            picture32 = request.picture32,
            picture96 = request.picture96,
        )
        userRepository.save(newUser)

        return newUser
    }

    @Transactional
    fun updateUser(request: UserUpdateRequest): User {
        val user = userQuerydslRepository.findByLoginTypeAndEmail(request.loginType, request.email)
            ?: throw IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        user.updateUser(
            displayName = request.displayName,
            password = request.password,
            newPassword = request.newPassword,
            picture32 = request.picture32,
            picture96 = request.picture96,
        )

        return user
    }

    @Transactional
    fun loginUser(request: UserLoginRequest): User {
        val user = userQuerydslRepository.findByLoginTypeAndEmail(request.loginType, request.email)
            ?: throw IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        if (!encoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("비밀번호가 틀렸습니다.")
        }
        return user

    }

    private fun getAccessToken(authorizationCode: String, registrationId: String): String {
        val clientId: String? = env.getProperty("oauth2.$registrationId.client-id")
        val clientSecret: String? = env.getProperty("oauth2.$registrationId.client-secret")
        val redirectUri: String? = env.getProperty("oauth2.$registrationId.redirect-uri")
        val tokenUri: String = env.getProperty("oauth2.$registrationId.token-uri")!!

        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("code", authorizationCode)
        params.add("client_id", clientId)
        params.add("client_secret", clientSecret)
        params.add("redirect_uri", redirectUri)
        params.add("grant_type", "authorization_code")

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        println(params)
        println(headers)

        val entity = HttpEntity(params, headers)

        println(entity)

        val responseNode: ResponseEntity<JsonNode> = restTemplate.exchange(
            tokenUri, HttpMethod.POST, entity,
            JsonNode::class.java
        )
        val accessTokenNode = responseNode.body
        return accessTokenNode?.get("access_token")?.asText()
            ?: throw IllegalArgumentException("토큰을 받아오지 못했습니다.")
    }

    private fun getUserResource(accessToken: String, registrationId: String): JsonNode? {
        val resourceUri = env.getProperty("oauth2.$registrationId.resource-uri")
        val headers = HttpHeaders()
        headers["Authorization"] = "Bearer $accessToken"
        val entity = HttpEntity<Any>(headers)
        return restTemplate.exchange(resourceUri!!, HttpMethod.GET, entity, JsonNode::class.java).body
    }

    @Transactional
    fun socialLogin(authorizationCode: String, registrationId: String): User {
        val accessToken = getAccessToken(authorizationCode, registrationId)
        val userResourceNode: JsonNode =
            getUserResource(accessToken, registrationId) ?: throw IllegalArgumentException("유저 정보를 받아오지 못했습니다.")
        println("userResourceNode = $userResourceNode")

        val id = userResourceNode["id"].asText()
        val email = userResourceNode["email"].asText()
        val nickname = userResourceNode["name"].asText()
        val picture: String? = userResourceNode["picture"]?.asText()

        val loginType = LoginType.GOOGLE

        return userQuerydslRepository.findByLoginTypeAndEmail(loginType, email) ?: run {

            val profileUrls = picture?.let {
                val pictureUrl = URI.create(picture).toURL()
                generateProfileUrls(pictureUrl)
            } ?: listOf(null, null)

            val newUser = User(
                snsSub = id,
                loginType = loginType,
                password = null,
                email = email,
                displayName = nickname,
                picture32 = profileUrls[0],
                picture96 = profileUrls[1],
            )
            userRepository.save(newUser)
            return newUser
        }
    }
}