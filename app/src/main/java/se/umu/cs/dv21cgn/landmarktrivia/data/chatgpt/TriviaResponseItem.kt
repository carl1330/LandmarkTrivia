package se.umu.cs.dv21cgn.landmarktrivia.data.chatgpt


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TriviaResponseItem(
    @SerialName("answer")
    val answer: Int,
    @SerialName("choices")
    val choices: List<String>,
    @SerialName("question")
    val question: String
)