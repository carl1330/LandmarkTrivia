package se.umu.cs.dv21cgn.landmarktrivia.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import se.umu.cs.dv21cgn.landmarktrivia.R
import se.umu.cs.dv21cgn.landmarktrivia.data.types.LocationResult
import se.umu.cs.dv21cgn.landmarktrivia.ui.composables.CitySearchBar
import se.umu.cs.dv21cgn.landmarktrivia.ui.composables.TriviaCard
import se.umu.cs.dv21cgn.landmarktrivia.ui.theme.LandmarkTriviaTheme

class LandmarkTrivia : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.GOOGLE_MAPS_KEY))
        }
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            val scope = CoroutineScope(Dispatchers.IO)
            val (cards, setCards) = remember { mutableStateOf(LocationResult()) }
            val locationLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                Toast.makeText(this, "Locations is $isGranted", Toast.LENGTH_SHORT).show()
            }
            val context = this

            @Suppress("DEPRECATION")
            fun getUserLocation(context: Context, requestPermissionLauncher: ActivityResultLauncher<String>, fusedLocationProviderClient: FusedLocationProviderClient) {
                val geocoder = Geocoder(context)
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener {
                            scope.launch {
                                var city = ""; var municipality = ""; var country = ""
                                val location = async { geocoder.getFromLocation(it.latitude, it.longitude, 1) }
                                val result = location.await()
                                //Since it seems like every location has different rules for what counts
                                //as a locality (City) this is the best i can do :(
                                if(location.isCompleted) {
                                    if (!result.isNullOrEmpty()) {
                                        if(!result[0].locality.isNullOrEmpty())
                                            city = result[0].locality
                                        if(!result[0].adminArea.isNullOrEmpty())
                                            municipality = result[0].adminArea
                                        if(!result[0].countryName.isNullOrEmpty())
                                            country = result[0].countryName
                                    }
                                    setCards(LocationResult(city, municipality, country))
                                }
                            }
                        }
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                        //SHOW DIALOG THAT SAYS IN ORDER TO USE THIS FEATURE YOU NEED TO ALLOW
                        //LOCATION PERMISSIONS
                    }
                    else -> {
                        requestPermissionLauncher.launch(
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                    }
                }
            }

            LandmarkTriviaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        CitySearchBar(setCards,
                            onGetLocationClick = {
                                getUserLocation(context, locationLauncher, fusedLocationClient)
                            })
                        LazyColumn(
                            modifier = Modifier.padding(0.dp, 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            content = {
                                if(cards.city != "") {
                                    item {
                                        TriviaCard(
                                            title = cards.city,
                                            description = "Learn more about ${cards.city} through a quick quiz!",
                                            onClick = {}
                                        )
                                    }
                                }
                                if(cards.municipality != "") {
                                    item {
                                        TriviaCard(
                                            title = cards.municipality,
                                            description = "Trivia quiz about the surrounding areas",
                                            onClick = {}
                                        )
                                    }
                                }
                                if(cards.country != "") {
                                    item {
                                        TriviaCard(
                                            title = cards.country,
                                            description = "Check your knowledge about the country of ${cards.country}",
                                            onClick = {}
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}





