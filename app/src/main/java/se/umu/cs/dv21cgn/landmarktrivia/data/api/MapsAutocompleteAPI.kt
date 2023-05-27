package se.umu.cs.dv21cgn.landmarktrivia.data.api

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import se.umu.cs.dv21cgn.landmarktrivia.data.types.LocationResult
import se.umu.cs.dv21cgn.landmarktrivia.data.types.SearchBarTuple

class MapsAPI(context: Context) {
    private val placesClient: PlacesClient = Places.createClient(context)
     fun mapsAutocomplete(query: String, options: (List<SearchBarTuple>) -> Unit) {
        val list = arrayListOf<SearchBarTuple>()
        val token = AutocompleteSessionToken.newInstance()
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setTypesFilter(listOf(PlaceTypes.CITIES))
                .setQuery(query)
                .build()



        val predictions = placesClient.findAutocompletePredictions(request)
        predictions.addOnSuccessListener {
            it.let {
                it.autocompletePredictions.forEach { prediction ->
                    list.add(SearchBarTuple(prediction.getPrimaryText(null).toString(), prediction.placeId))
                }
            }
            options(list)
        }
    }

    fun mapsFetchPlace(
        id: String,
        searchBarValue: String,
        setLocationResult: (LocationResult) -> Unit
    ) {
        val token = AutocompleteSessionToken.newInstance()
        val list = arrayListOf(Place.Field.ADDRESS_COMPONENTS)
        val request = FetchPlaceRequest.builder(id, list)
            .setSessionToken(token)
            .build()

        val location = LocationResult(searchBarValue)

        val response = placesClient.fetchPlace(request)
        response.addOnSuccessListener {
            it.place.addressComponents?.asList()?.forEach { result ->
                if(result.types.contains(PlaceTypes.ADMINISTRATIVE_AREA_LEVEL_1)) {
                    location.municipality = result.name
                }
                else if(result.types.contains(PlaceTypes.COUNTRY)) {
                    location.country = result.name
                }
            }
            setLocationResult(location)
        }
    }
}
