package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviagamescreen

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviagamescreen.components.QuestionPanel

/**
 * The trivia game screen, on start will show a loading icon until trivia questions have
 * finished generating.
 * If an error occurs it will show an AlertDialog displaying the error message to the user.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriviaGameScreen(
    viewModel: TriviaGameViewModel = hiltViewModel(),
    navController: NavController,
    title: String,
    type: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val shareIntent = createShareIntent(viewModel, type, title)
        val context = LocalContext.current

        Column {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back to main menu")
                    }
                }
            )
            if (viewModel.isLoading.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if(viewModel.error.value) {
                    if (viewModel.errorMessage.value != "") {
                        AlertDialog(
                            onDismissRequest = {
                                viewModel.confirmError()
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    viewModel.confirmError()
                                }) {
                                    Text(text = "Confirm")
                                }
                            },
                            title = { Text(text = "Error") },
                            text = { Text(text = viewModel.errorMessage.value) },
                        )
                    }
                } else {
                    if (!viewModel.gameOverState.value) {
                        val (selectedOption, onOptionSelected) = remember { mutableStateOf("") }
                        LazyColumn(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterVertically
                            ),
                            horizontalAlignment = Alignment.End
                        ) {
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Question ${viewModel.currentQuestionIndex.value + 1}",
                                    textAlign = TextAlign.Center,
                                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                                )
                                QuestionPanel(
                                    question = viewModel.state.value[viewModel.currentQuestionIndex.value].question,
                                    choices = viewModel.state.value[viewModel.currentQuestionIndex.value].choices,
                                    selectedOption = selectedOption,
                                    onOptionSelected = onOptionSelected
                                )
                                Button(
                                    onClick = {
                                        viewModel.checkAnswer(
                                            viewModel.state.value[viewModel.currentQuestionIndex.value].choices.indexOf(
                                                selectedOption
                                            )
                                        )
                                        viewModel.goToNextQuestion()
                                    }
                                ) {
                                    Text(text = "Answer")
                                }
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterVertically
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Results",
                                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "You got ${viewModel.score.value}/5 questions correct!\n\uD83C\uDF89\uD83E\uDD73",
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = {
                                    context.startActivity(shareIntent)
                                },
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Icon(
                                        Icons.Filled.Share,
                                        contentDescription = "Share result on social media"
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Share")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Creates a new Android Sharesheet intent where the user
 * can then share their score for example social media apps.
 * https://developer.android.com/training/sharing/send#using-android-system-sharesheet
 */
private fun createShareIntent(
    viewModel: TriviaGameViewModel,
    type: String,
    title: String
): Intent {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "I just scored ${viewModel.score.value}/5 on a quiz about the $type of $title on LandmarkTrivia! \uD83C\uDF89\uD83E\uDD73"
        )
        this.type = "text/plain"
    }
    return Intent.createChooser(sendIntent, null)
}


