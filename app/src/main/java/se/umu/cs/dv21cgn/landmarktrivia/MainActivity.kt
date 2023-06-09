package se.umu.cs.dv21cgn.landmarktrivia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint
import se.umu.cs.dv21cgn.landmarktrivia.ui.theme.LandmarkTriviaTheme
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.Screen
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.TriviaCardListScreen
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviagamescreen.TriviaGameScreen

/**
 * This activity is used mainly for initializing the Place client as well as
 * setting up the navcontroller.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initialize(this, BuildConfig.PLACE_API_KEY)
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
                            TriviaCardListScreen(navController)
                        }
                        composable(
                            route = Screen.TriviaGameScreen.route + "/{type}/{title}",
                            arguments = listOf(
                                navArgument("type") {
                                    type = NavType.StringType
                                },
                                navArgument("title") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val type = it.arguments?.getString("type")!!
                            val title = it.arguments?.getString("title")!!

                            TriviaGameScreen(
                                navController = navController,
                                title = title,
                                type = type
                            )
                        }
                    }
                }
            }
        }
    }
}