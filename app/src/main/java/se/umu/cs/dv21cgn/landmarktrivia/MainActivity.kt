package se.umu.cs.dv21cgn.landmarktrivia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.Places
import se.umu.cs.dv21cgn.landmarktrivia.data.types.LocationResult
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
                    LandmarkTrivia()
                }
            }
        }
    }
}

@Composable
fun LandmarkTrivia() {
    val (cards, setCards) = remember { mutableStateOf(LocationResult()) }
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    ) {
        CitySearchBar(setCards)
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
        })
    }
}


