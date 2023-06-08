package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviagamescreen

import se.umu.cs.dv21cgn.landmarktrivia.data.chatgpt.TriviaResponse

data class TriviaGameState (
    val title: String = "",
    val score: Int = 0,
    val triviaQuestions: TriviaResponse = TriviaResponse()
)