package se.umu.cs.dv21cgn.landmarktrivia.ui.composables
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import se.umu.cs.dv21cgn.landmarktrivia.data.api.MapsAPI
import se.umu.cs.dv21cgn.landmarktrivia.data.types.LocationResult
import se.umu.cs.dv21cgn.landmarktrivia.data.types.SearchBarTuple

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun CitySearchBar(
    setLocationResult: (LocationResult) -> Unit,
    onGetLocationClick: () -> Unit,
) {
    var searchBarValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val (options, setOptions) = remember { mutableStateOf(listOf<SearchBarTuple>()) }
    val context = LocalContext.current
    val mapsApi = MapsAPI(context)

    fun updateOptions() {
        mapsApi.mapsAutocomplete(searchBarValue, setOptions)
    }

    fun updateLocation(id: String) {
        mapsApi.mapsFetchPlace(id, searchBarValue, setLocationResult)
    }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            value = searchBarValue,
            onValueChange = {
                searchBarValue = it
                expanded = true
                updateOptions()
            },
            shape = RoundedCornerShape(50.dp),
            trailingIcon = {
                IconButton(onClick = onGetLocationClick) {
                    Icon(Icons.Outlined.LocationOn, "")
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        val filteringOptions = options.filter { it.city.contains(searchBarValue, ignoreCase = true) }
        if (filteringOptions.isNotEmpty()) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = false),
                modifier = Modifier.exposedDropdownSize(matchTextFieldWidth = true)
            ) {
                filteringOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.city) },
                        onClick = {
                            searchBarValue = selectionOption.city
                            updateLocation(selectionOption.id)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}