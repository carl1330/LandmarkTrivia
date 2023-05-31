package se.umu.cs.dv21cgn.landmarktrivia.data

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import se.umu.cs.dv21cgn.landmarktrivia.data.placeapi.AutocompletePredictionResponse

interface TriviaCardListRepository {
    fun getPlacePredictionsByQuery(query: String): Task<FindAutocompletePredictionsResponse>
    fun getPlacePhotoById(photoMetadata: PhotoMetadata): Task<FetchPhotoResponse>
    fun getPlaceById(id: String): Task<FetchPlaceResponse>
}