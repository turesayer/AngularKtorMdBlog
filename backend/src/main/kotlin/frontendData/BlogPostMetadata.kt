package koeln.sayer.frontendData

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class BlogPostMetadata(
    val title: String,
    val date: LocalDate,
    val filename: String,
    val visibility: Visibility
)

enum class Visibility {
    PUBLIC, UNLISTED
}
