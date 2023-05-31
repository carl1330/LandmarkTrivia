package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.components.SearchBar
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.components.TriviaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriviaCardListScreen(
    viewModel: TriviaCardListViewModel = hiltViewModel()
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 8.dp, 0.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchBar(viewModel = viewModel)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                viewModel.state.value.triviaCards.forEach { card ->
                    item {
                        TriviaCard(
                            title = card.title,
                            description = card.description,
                            onClick = { /*TODO*/ },
                            isFetching = false,
                            image = card.image
                        )
                    }
                }
            })

        }
    }
}