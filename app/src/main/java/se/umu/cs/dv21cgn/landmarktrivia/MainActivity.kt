package se.umu.cs.dv21cgn.landmarktrivia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint
import se.umu.cs.dv21cgn.landmarktrivia.ui.theme.LandmarkTriviaTheme
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.Screen
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.TriviaCardListScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initialize(this, "AIzaSyC-SrIdW411DQVE5J5I2_sc27ncbve-el4");
        }
        setContent {
            LandmarkTriviaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.TriviaCardScreen.route
                    ) {
                        composable(
                            route = Screen.TriviaCardScreen.route
                        ) {
                            TriviaCardListScreen()
                        }
                    }
                }
            }
        }
    }
}