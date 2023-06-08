package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.places.api.model.PlaceLikelihood
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
    private val repository: TriviaCardListRepository,
) : ViewModel() {

    private val _state = mutableStateOf(TriviaCardListState())
    val state: State<TriviaCardListState> = _state

    private val _city = mutableStateOf(TriviaCardState())
    private val _admin = mutableStateOf(TriviaCardState())
    private val _country = mutableStateOf(TriviaCardState())

    val city: State<TriviaCardState> = _city
    val admin: State<TriviaCardState> = _admin
    val country: State<TriviaCardState> = _country


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
            _state.value = TriviaCardListState(predictionList, state.value.locationId)
        }
    }

    fun updateLocation(id: String) {
        val response = repository.getPlaceById(id)
        response.addOnSuccessListener { result ->
            result.place.addressComponents?.asList()?.forEach { location ->
                when {
                    location.types.contains(PlaceTypes.LOCALITY) || location.types.contains(PlaceTypes.ADMINISTRATIVE_AREA_LEVEL_3) || location.types.contains(PlaceTypes.ADMINISTRATIVE_AREA_LEVEL_2) || location.types.contains(PlaceTypes.SUBLOCALITY) -> {
                        _city.value =
                            TriviaCardState(
                                title = location.name,
                                description = "Learn more about ${location.name} through a quick quiz",
                                loading = true
                            )
                        updateCardPhoto(result, _city)
                    }
                    location.types.contains(PlaceTypes.ADMINISTRATIVE_AREA_LEVEL_1) -> {
                        _admin.value =
                            TriviaCardState(
                                title = location.name,
                                description = "Learn more about ${location.name} through a quick quiz",
                                loading = true
                            )
                        updateCardPhoto(result, _admin)
                    }
                    location.types.contains(PlaceTypes.COUNTRY) -> {
                        _country.value =
                            TriviaCardState(
                                title = location.name,
                                description = "Learn more about ${location.name} through a quick quiz",
                                loading = true
                            )
                        updateCardPhoto(result, _country)
                    }
                }
            }
        }
    }

    private fun updateCardPhoto(result: FetchPlaceResponse, state: MutableState<TriviaCardState>, ) {
        val metadata = result.place.photoMetadatas
        if (metadata == null || metadata.isEmpty())
            state.value = TriviaCardState(state.value.title, state.value.description, null, false)
        else {
            repository.getPlacePhotoById(metadata.random())
                .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                    val bitmap = fetchPhotoResponse.bitmap
                    state.value =
                        TriviaCardState(state.value.title, state.value.description, bitmap, false)
                }
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    fun updateLocationWithCurrentLocation() {
        val response = repository.getCurrentPlace()
        response.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val place = task.result.placeLikelihoods.first().place
                updateLocation(place.id)
            } else {
                val exception = task.exception
                if (exception is ApiException) {
                    //TODO Add error state when location of user could not be fetched.
                }
            }
        }
    }


}