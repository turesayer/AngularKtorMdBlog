package koeln.sayer.frontendData

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class BlogPostMetadata(
    val title: String,
    val date: LocalDate,
    val filename: String,
    val visibility: Visibility
) {
    companion object {
        fun toDto(metadata: BlogPostMetadata): BlogPostMetadataDto =
            BlogPostMetadataDto(metadata.title, metadata.date, metadata.filename)
    }
}

@Serializable
data class BlogPostMetadataDto(
    val title: String,
    val date: LocalDate,
    val filename: String,
)

enum class Visibility {
    PUBLIC, UNLISTED
}
