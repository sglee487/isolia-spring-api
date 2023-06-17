package com.group.isolia_api.service

import org.springframework.web.multipart.MultipartFile
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File

class ImageUtils {
    companion object {
        fun changeImageMultipartFileToFile(multipartFile: MultipartFile): File {
            val file = File.createTempFile("image", ".jpg")
            multipartFile.transferTo(file)
            return file
        }

        fun resizeImage(image: BufferedImage, width: Int, height: Int): BufferedImage {
            val resizedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
            val graphics = resizedImage.createGraphics()
            graphics.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null)
            graphics.dispose()
            return resizedImage
        }
    }
}