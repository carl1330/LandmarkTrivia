package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.Screen
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.components.SearchBar
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.components.TriviaCard

/**
 * Main screen of the application
 */
@Composable
fun TriviaCardListScreen(
    navController: NavController,
    viewModel: TriviaCardListViewModel = hiltViewModel()
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp, 16.dp, 16.dp, 0.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchBar(
                viewModel = viewModel,
            )
            if(viewModel.state.value.shouldShowRationale) {
                AlertDialog(
                    onDismissRequest = { viewModel.disableShouldShowRationale() },
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    title = { Text(text = "Location permission is required", textAlign = TextAlign.Center) },
                    text = { Text(text = "This functionality requires Location permissions, please enable them for LandmarkTrivia in your device settings", textAlign = TextAlign.Center) },
                    confirmButton = { TextButton(onClick = { viewModel.disableShouldShowRationale() }) {
                        Text(text = "Understood")
                    } }
                )
            }
            if(viewModel.state.value.isLoading) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                if(viewModel.city.value.title.isNotEmpty()) {
                    item {
                        TriviaCard(
                            title = viewModel.city.value.title,
                            description = viewModel.city.value.description,
                            onClick = {
                                navController.navigate(Screen.TriviaGameScreen.route + "/city/${viewModel.city.value.title}")
                            },
                            image = viewModel.city.value.image,
                            isFetching = viewModel.city.value.loading,
                        )
                    }
                }
                if(viewModel.admin.value.title.isNotEmpty()) {
                    item {
                        TriviaCard(
                            title = viewModel.admin.value.title,
                            description = viewModel.admin.value.description,
                            onClick = {
                                navController.navigate(Screen.TriviaGameScreen.route + "/district/${viewModel.admin.value.title}")
                            },
                            image = viewModel.admin.value.image,
                            isFetching = viewModel.admin.value.loading
                        )
                    }
                }
                if(viewModel.country.value.title.isNotEmpty()) {
                    item {
                        TriviaCard(
                            title = viewModel.country.value.title,
                            description = viewModel.country.value.description,
                            onClick = {
                                navController.navigate(Screen.TriviaGameScreen.route + "/country/${viewModel.country.value.title}")
                            },
                            image = viewModel.country.value.image,
                            isFetching = viewModel.country.value.loading
                        )
                    }
                }
            })
        }
    }
}