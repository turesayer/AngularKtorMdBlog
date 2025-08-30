package koeln.sayer.frontendData

import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import koeln.sayer.logger
import kotlinx.serialization.json.Json
import java.io.File

class FrontendDataService(
    private val postsFileLocations: String
) {
    fun getAllPosts(): List<BlogPostMetadataDto> {
        val dir = File("$postsFileLocations/metadata.json")
        if (dir.exists()) {
            val rawMetadataFile = File("$postsFileLocations/metadata.json").readText()
            val postMetadataList: List<BlogPostMetadata> = try {
                Json.decodeFromString(rawMetadataFile)
            } catch (e: Exception) {
                logger().error("Error while parsing metadata.json", e)
                emptyList()
            }

            return postMetadataList
                .filter { verifyBlogPostExists(it.filename) }
                .filter { it.visibility == Visibility.PUBLIC }
                .sortedByDescending { it.date }
                .map { BlogPostMetadata.toDto(it) }
        } else {
            logger().info("Post directory with metadata.json does not exist")
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

    private fun verifyBlogPostExists(filename: String): Boolean =
        File("$postsFileLocations/$filename").exists()
}