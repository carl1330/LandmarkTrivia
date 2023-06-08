package se.umu.cs.dv21cgn.landmarktrivia.data

import android.Manifest
import androidx.annotation.RequiresPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import se.umu.cs.dv21cgn.landmarktrivia.data.placeapi.AutocompletePredictionResponse

interface TriviaCardListRepository {
    fun getPlacePredictionsByQuery(query: String): Task<FindAutocompletePredictionsResponse>
    fun getPlacePhotoById(photoMetadata: PhotoMetadata): Task<FetchPhotoResponse>
    fun getPlaceById(id: String): Task<FetchPlaceResponse>

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    fun getCurrentPlace() : Task<FindCurrentPlaceResponse>
}