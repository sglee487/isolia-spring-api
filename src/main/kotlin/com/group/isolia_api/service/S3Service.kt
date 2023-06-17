package com.group.isolia_api.service

import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO

@Component
@Service
class S3Service(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    @Value("\${cloud.aws.credentials.access-key}")
    private val accessKey: String,
    @Value("\${cloud.aws.credentials.secret-key}")
    private val secretKey: String
) {

    private val credentials = BasicAWSCredentials(accessKey, secretKey)
    private val s3Client = AmazonS3ClientBuilder
        .standard()
        .withCredentials(AWSStaticCredentialsProvider(credentials))
        .withRegion(Regions.AP_NORTHEAST_2)
        .build()

    fun uploadToS3(uploadFilePath: String, file: File): URL {
        try {
            s3Client.putObject(bucket, uploadFilePath, file)
            return s3Client.getUrl(bucket, uploadFilePath) ?: throw Exception("S3에 업로드한 파일의 URL을 가져오는데 실패했습니다.")
        } catch (e: AmazonServiceException) {
            System.err.println(e.errorMessage)
            throw e
        }
    }

    fun uploadImageToS3(uploadImage: BufferedImage, uploadFilePath: String): URL {
        try {
            val fileName = UUID.randomUUID().toString()
            val tempFile = Paths.get("temp",fileName).toFile()
            ImageIO.write(uploadImage, "jpg", tempFile)
            s3Client.putObject(bucket, uploadFilePath, tempFile)
            tempFile.delete()
            return s3Client.getUrl(bucket, uploadFilePath)
        } catch (e: AmazonServiceException) {
            System.err.println(e.errorMessage)
            throw e
        }
    }

    fun getUrlFromS3(uploadFilePath: String): URL {
        try {
            return s3Client.getUrl(bucket, uploadFilePath)
        } catch (e: AmazonServiceException) {
            System.err.println(e.errorMessage)
            throw e
        }
    }
}