package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.components

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.TriviaCardListViewModel

/**
 * The searchbar composable, accepts either user input or the user can select to
 * update their location by using the devices location.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalComposeUiApi::class
)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun SearchBar(
    viewModel: TriviaCardListViewModel,
) {
    var searchBarValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val options = viewModel.state.value.placePredictions
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current


    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            value = searchBarValue,
            onValueChange = {
                searchBarValue = it
                expanded = true
                viewModel.updatePredictions(it)
            },
            shape = RoundedCornerShape(50.dp),
            trailingIcon = {
                Icon(Icons.Outlined.Search, contentDescription = "Location search bar")
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        val filteringOptions = options.filter { it.placePrimaryText.contains(searchBarValue, ignoreCase = true) }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            properties = PopupProperties(focusable = false),
            modifier = Modifier.exposedDropdownSize(matchTextFieldWidth = true)
        ) {
            if(LocalContext.current.packageManager.hasSystemFeature(
                    PackageManager.FEATURE_LOCATION_GPS
            )) {
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Use current location")
                            Icon(Icons.Outlined.LocationOn, contentDescription = "")
                        }
                    },
                    onClick = {
                        if(ContextCompat.checkSelfPermission(
                            context,
                            locationPermissionState.permission
                        ) == PackageManager.PERMISSION_GRANTED) {
                            viewModel.updateLocationWithCurrentLocation()
                        }
                        else if(ActivityCompat.shouldShowRequestPermissionRationale(
                                context as Activity,
                                locationPermissionState.permission
                            )) {
                            viewModel.enableShouldShowRationale()
                        } else {
                            locationPermissionState.launchPermissionRequest()
                        }
                        expanded = false
                        keyboardController?.hide()
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
            if (filteringOptions.isNotEmpty()) {
                filteringOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.placeFullText) },
                        onClick = {
                            searchBarValue = selectionOption.placeFullText
                            expanded = false
                            viewModel.updateLocation(selectionOption.placeId)
                            keyboardController?.hide()
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}