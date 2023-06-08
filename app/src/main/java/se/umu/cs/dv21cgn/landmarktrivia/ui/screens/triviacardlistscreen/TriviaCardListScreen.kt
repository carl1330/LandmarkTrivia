package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.Screen
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.components.SearchBar
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.components.TriviaCard

@Composable
fun TriviaCardListScreen(
    navController: NavController,
    viewModel: TriviaCardListViewModel = hiltViewModel()
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp, 8.dp, 8.dp, 0.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchBar(
                viewModel = viewModel,
            )
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
                            onClick = { /*TODO*/ },
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
                            onClick = { /*TODO*/ },
                            image = viewModel.country.value.image,
                            isFetching = viewModel.country.value.loading
                        )
                    }
                }
            })
        }
    }
}