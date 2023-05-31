package se.umu.cs.dv21cgn.landmarktrivia.data

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

class TriviaCardListImpl(
    private val client: PlacesClient
) : TriviaCardListRepository {
    override fun getPlacePredictionsByQuery(query: String): Task<FindAutocompletePredictionsResponse> {
        val token = AutocompleteSessionToken.newInstance()
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setTypesFilter(listOf(PlaceTypes.CITIES))
                .setSessionToken(token)
                .setQuery(query)
                .build()

        return client.findAutocompletePredictions(request)
    }

    override fun getPlacePhotoById(photoMetadata: PhotoMetadata): Task<FetchPhotoResponse> {
        val request =
            FetchPhotoRequest.builder(photoMetadata)
                .build()

        return client.fetchPhoto(request)
    }

    override fun getPlaceById(id: String): Task<FetchPlaceResponse> {
        val token = AutocompleteSessionToken.newInstance()
        val request =
                FetchPlaceRequest.builder(id, listOf(Place.Field.ADDRESS_COMPONENTS, Place.Field.PHOTO_METADATAS))
                    .setSessionToken(token)
                    .build()

        return client.fetchPlace(request)
    }
}
