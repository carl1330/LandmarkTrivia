package se.umu.cs.dv21cgn.landmarktrivia.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerialName


class UnsplashAPI(private val apiKey: String) {
    private val baseURL = "api.unsplash.com"

    suspend fun getPhotoByKeyword(query: String): UnsplashResponse {
        val response: UnsplashResponse = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
        }.use { client ->
            client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = baseURL
                    path("search/photos")
                    parameters.append("query", query)
                    parameters.append("client_id", apiKey)
                    parameters.append("orientation", "landscape")
                }
            }
        }.body()

        return response
    }
}

@Serializable
data class UnsplashResponse(
    @SerialName("results")
    val results: List<Result>?,
    @SerialName("total")
    val total: Int?,
    @SerialName("total_pages")
    val totalPages: Int?
)

@Serializable
data class Result(
    @SerialName("blur_hash")
    val blurHash: String?,
    @SerialName("color")
    val color: String?,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("current_user_collections")
    val currentUserCollections: List<@Contextual Any>?,
    @SerialName("description")
    val description: String?,
    @SerialName("height")
    val height: Int?,
    @SerialName("id")
    val id: String?,
    @SerialName("liked_by_user")
    val likedByUser: Boolean?,
    @SerialName("likes")
    val likes: Int?,
    @SerialName("links")
    val links: Links?,
    @SerialName("urls")
    val urls: Urls?,
    @SerialName("user")
    val user: User?,
    @SerialName("width")
    val width: Int?
)

@Serializable
data class Links(
    @SerialName("download")
    val download: String?,
    @SerialName("html")
    val html: String?,
    @SerialName("self")
    val self: String?
)

@Serializable
data class Urls(
    @SerialName("full")
    val full: String?,
    @SerialName("raw")
    val raw: String?,
    @SerialName("regular")
    val regular: String?,
    @SerialName("small")
    val small: String?,
    @SerialName("thumb")
    val thumb: String?
)

@Serializable
data class User(
    @SerialName("first_name")
    val firstName: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("instagram_username")
    val instagramUsername: String?,
    @SerialName("last_name")
    val lastName: String?,
    @SerialName("links")
    val links: LinksX?,
    @SerialName("name")
    val name: String?,
    @SerialName("portfolio_url")
    val portfolioUrl: String?,
    @SerialName("profile_image")
    val profileImage: ProfileImage?,
    @SerialName("twitter_username")
    val twitterUsername: String?,
    @SerialName("username")
    val username: String?
)

@Serializable
data class LinksX(
    @SerialName("html")
    val html: String?,
    @SerialName("likes")
    val likes: String?,
    @SerialName("photos")
    val photos: String?,
    @SerialName("self")
    val self: String?
)

@Serializable
data class ProfileImage(
    @SerialName("large")
    val large: String?,
    @SerialName("medium")
    val medium: String?,
    @SerialName("small")
    val small: String?
)