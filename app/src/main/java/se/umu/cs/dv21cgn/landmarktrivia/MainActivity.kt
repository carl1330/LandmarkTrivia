package se.umu.cs.dv21cgn.landmarktrivia

import android.os.Bundle
import android.widget.AutoCompleteTextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.android.libraries.places.api.Places
import se.umu.cs.dv21cgn.landmarktrivia.ui.composables.CitySearchBar
import se.umu.cs.dv21cgn.landmarktrivia.ui.composables.TriviaCard
import se.umu.cs.dv21cgn.landmarktrivia.ui.theme.LandmarkTriviaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.GOOGLE_MAPS_KEY))
        }
        setContent {
            LandmarkTriviaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        CitySearchBar()
                        TriviaCard(title = "Umeå", description = "Oskar är inte en bajs") {

                        }
                    }
                }
            }
        }
    }
}
