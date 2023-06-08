package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviagamescreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import se.umu.cs.dv21cgn.landmarktrivia.data.chatgpt.TriviaResponse
import javax.inject.Inject

@HiltViewModel
class TriviaGameViewModel @Inject constructor(

) : ViewModel() {
    private val gson = Gson()
    private val _state = gson.fromJson("[\n" +
            "{\n" +
            "\"question\": \"What is the population of Saga City?\",\n" +
            "\"choices\": [\"Approximately 80,000\", \"Approximately 150,000\", \"Approximately 200,000\", \"Approximately 300,000\"],\n" +
            "\"answer\": 2\n" +
            "},\n" +
            "{\n" +
            "\"question\": \"Which famous castle is located in Saga City?\",\n" +
            "\"choices\": [\"Himeji Castle\", \"Nijo Castle\", \"Kumamoto Castle\", \"Tsuwano Castle\"],\n" +
            "\"answer\": 3\n" +
            "},\n" +
            "{\n" +
            "\"question\": \"What is the traditional craft of Saga City?\",\n" +
            "\"choices\": [\"Yuzen dyeing\", \"Washi papermaking\", \"Arita porcelain\", \"Kaga Yuzen silk dyeing\"],\n" +
            "\"answer\": 2\n" +
            "},\n" +
            "{\n" +
            "\"question\": \"Which famous festival takes place in Saga City every October?\",\n" +
            "\"choices\": [\"Gion Matsuri\", \"Awa Odori\", \"Kishiwada Danjiri Matsuri\", \"Karatsu Kunchi\"],\n" +
            "\"answer\": 3\n" +
            "},\n" +
            "{\n" +
            "\"question\": \"Which prefecture is Saga City located in?\",\n" +
            "\"choices\": [\"Hiroshima\", \"Saga\", \"Kumamoto\", \"Yamaguchi\"],\n" +
            "\"answer\": 1\n" +
            "}\n" +
            "]", TriviaResponse::class.java)
    val state: TriviaResponse = _state

    private val _currentQuestionIndex = mutableIntStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex

    private val _gameOverState = mutableStateOf(false)
    val gameOverState: State<Boolean> = _gameOverState

    private var _score = mutableIntStateOf(0)
    val score: State<Int> = _score

    fun checkAnswer(choice: Int) {
        if(state[currentQuestionIndex.value].answer == choice) {
            _score.intValue++
        }
    }

    fun goToNextQuestion() {
        if(currentQuestionIndex.value + 1 < state.size) {
            _currentQuestionIndex.intValue++
        } else {
            _gameOverState.value = true
        }
    }
}