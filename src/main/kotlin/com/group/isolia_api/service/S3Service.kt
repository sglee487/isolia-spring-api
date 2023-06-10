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
import java.nio.file.Paths

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

    private fun changeImageMultipartFileToFile(multipartFile: MultipartFile): File {
        val file = File.createTempFile("image", ".jpg")
        multipartFile.transferTo(file)
        return file
    }

    fun generateImageUrl(file: MultipartFile, uploadPath: String): String {
        val fileName = file.originalFilename ?: throw IllegalArgumentException("Invalid file name")
        val (fileNameNoExt, fileExtension) =  fileName.split(".")
        val uploadFileName = "${fileNameNoExt}_${System.currentTimeMillis()}.$fileExtension"
        val uploadFilePath = Paths.get(uploadPath, uploadFileName).toString()

        try {
            s3Client.putObject(bucket, uploadFilePath, changeImageMultipartFileToFile(file))
            val url = s3Client.getUrl(bucket, uploadFilePath)
            return url.toString()
        } catch (e: AmazonServiceException) {
            System.err.println(e.errorMessage)
            throw e
        }
    }
}