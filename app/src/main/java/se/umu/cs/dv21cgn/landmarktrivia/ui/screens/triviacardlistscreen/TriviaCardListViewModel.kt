package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import se.umu.cs.dv21cgn.landmarktrivia.data.TriviaCardListRepository
import se.umu.cs.dv21cgn.landmarktrivia.data.placeapi.AutocompletePredictionResponse
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.components.TriviaCardState
import javax.inject.Inject

@HiltViewModel
class TriviaCardListViewModel @Inject constructor(
    private val repository: TriviaCardListRepository
) : ViewModel() {

    private val _state = mutableStateOf(TriviaCardListState())
    val state: State<TriviaCardListState> = _state

    fun updatePredictions(query: String) {
        val predictionList = arrayListOf<AutocompletePredictionResponse>()
        val response = repository.getPlacePredictionsByQuery(query)
        response.addOnSuccessListener { result ->
            for (prediction in result.autocompletePredictions) {
                predictionList.add(
                    AutocompletePredictionResponse(
                    placeId = prediction.placeId,
                    placePrimaryText = prediction.getPrimaryText(null).toString(),
                    placeFullText = prediction.getFullText(null).toString()
                ))
            }
            _state.value = TriviaCardListState(state.value.triviaCards, predictionList, state.value.locationId)
        }
    }

    fun updateLocation(id: String) {
        val response = repository.getPlaceById(id)
        val locationList = arrayListOf<TriviaCardState>()
        response.addOnSuccessListener { result ->
            var name = ""
            var description = ""
            result.place.addressComponents?.asList()?.forEach { location ->
                when {
                    location.types.contains(PlaceTypes.LOCALITY) -> {
                        name = location.name
                        description = "Learn more about ${location.name} through a quick quiz"
                    }
                    location.types.contains(PlaceTypes.ADMINISTRATIVE_AREA_LEVEL_1) -> {
                        name = location.name
                        description = "Learn more about ${location.name} through a quick quiz"
                    }
                    location.types.contains(PlaceTypes.COUNTRY) -> {
                        name = location.name
                        description = "Learn more about ${location.name} through a quick quiz"
                    }
                }
                locationList.add(
                    TriviaCardState(
                        title = name,
                        description = description,
                    )
                )
                _state.value = TriviaCardListState(locationList, state.value.placePredictions, locationId = id)
            }
        }
    }
}