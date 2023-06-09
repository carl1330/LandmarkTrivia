package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen
import se.umu.cs.dv21cgn.landmarktrivia.data.placeapi.AutocompletePredictionResponse

data class TriviaCardListState(
    val placePredictions: List<AutocompletePredictionResponse> = listOf(),
    val locationId: String = "",
    val isLoading: Boolean = false,
    val shouldShowRationale: Boolean = false
)
