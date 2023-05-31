package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen

import se.umu.cs.dv21cgn.landmarktrivia.data.placeapi.AutocompletePredictionResponse
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.components.TriviaCardState

data class TriviaCardListState(
    val triviaCards: List<TriviaCardState> = listOf(),
    val placePredictions: List<AutocompletePredictionResponse> = listOf(),
    val locationId: String = "",
    val isLoading: Boolean = false
)
