package com.group.isolia_api.service

import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.net.URL

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

    fun changeImageMultipartFileToFile(multipartFile: MultipartFile): File {
        val file = File.createTempFile("image", ".jpg")
        multipartFile.transferTo(file)
        return file
    }

    fun uploadToS3(uploadFilePath: String, file: File): String {
        try {
            s3Client.putObject(bucket, uploadFilePath, file)
            val url = s3Client.getUrl(bucket, uploadFilePath)
            return url.toString()
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