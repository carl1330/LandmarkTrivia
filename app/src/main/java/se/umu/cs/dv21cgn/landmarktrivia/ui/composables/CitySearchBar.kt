package se.umu.cs.dv21cgn.landmarktrivia.ui.composables
import android.content.Context
import android.util.Log
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearchBar() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
    val options = remember { mutableStateOf(listOf("")) }
    val context = LocalContext.current

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor(),
            value = selectedOptionText,
            onValueChange = {
                selectedOptionText = it
                expanded = true
                mapsAutocomplete(selectedOptionText, context, options)
            },
        )
        // filter options based on text field value
        val filteringOptions = options.value.filter { it.contains(selectedOptionText, ignoreCase = true) }
        if (filteringOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                filteringOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

private fun mapsAutocomplete(query: String, context: Context, mutableOptions: MutableState<List<String>>) {
    val token = AutocompleteSessionToken.newInstance()
    val placesClient = Places.createClient(context)
    val options = arrayListOf<String>()
    val request =
        FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setTypesFilter(listOf(PlaceTypes.CITIES))
            .setQuery(query)
            .build()

    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
            for (prediction in response.autocompletePredictions) {
                options.add(prediction.getPrimaryText(null).toString())
            }
            mutableOptions.value = options
        }.addOnFailureListener { exception: Exception? ->
            if (exception is ApiException) {
                Log.e("MAPS_API", "Place not found: ${exception.statusCode}")
            }
        }
}

