package koeln.sayer.frontendData

import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import koeln.sayer.logger
import java.io.File

class FrontendDataService (
    private val postsFileLocations: String
) {
    fun getAllPosts(): List<String> {
        val dir = File(postsFileLocations)
        if (dir.exists()) {
            val files = dir.listFiles()?.toList() ?: emptyList()
            logger().debug("Found ${files.size} posts")
            return files.map { it.name }.sortedDescending()
        } else {
            logger().info("Post directory does not exist")
            return emptyList()
        }
    }

    fun getPostContent(filename: String): String {
        if (filename.isBlank()) {
            throw BadRequestException("Filename must not be blank")
        }
        val file = File("$postsFileLocations/$filename")
        logger().info("Reading post file $postsFileLocations/$filename")
        if (!file.exists()) {
            logger().info("Post file $filename does not exist")
            throw NotFoundException()
        }
        return file.readText()
    }
}