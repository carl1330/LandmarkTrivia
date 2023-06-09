package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviagamescreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.exception.OpenAIException
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import se.umu.cs.dv21cgn.landmarktrivia.data.chatgpt.TriviaResponse
import javax.inject.Inject

@OptIn(BetaOpenAI::class)
@HiltViewModel
class TriviaGameViewModel @Inject constructor(
    private val openAI: OpenAI,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val gson = Gson()
    @SuppressLint("MutableCollectionMutableState")
    private var _state = mutableStateOf(TriviaResponse())
    val state: State<TriviaResponse> = _state

    private val _currentQuestionIndex = mutableIntStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex

    private val _gameOverState = mutableStateOf(false)
    val gameOverState: State<Boolean> = _gameOverState

    private var _score = mutableIntStateOf(0)
    val score: State<Int> = _score

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    private val _error = mutableStateOf(false)
    val error: State<Boolean> = _error

    /**
     * Generate trivia questions using GPT-3.5-Turbo
     * When finished sets the loading state to false.
     */
    init {
        val type = savedStateHandle.get<String>("type")
        val title = savedStateHandle.get<String>("title")
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = "Create a list of five random trivia questions about the $type of $title\n" +
                            "Do not include any explanations, only provide a  RFC8259 compliant JSON response  following this format without deviation.\n" +
                            "[{\n" +
                            "      \"question\": \"A random trivia question\",\n" +
                            "      \"choices\": [\"Option 1\", \"Option 2\", \"Option 3\", \"Option 4\"],\n" +
                            "      \"answer\": The correct options index\n" +
                            "}]\n" +
                            "The JSON response:"
                )
            )
        )
        viewModelScope.launch {
            try {
                val response: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
                if(response.choices.isNotEmpty() && response.choices[0].finishReason == "stop") {
                    _state.value = gson.fromJson(response.choices[0].message?.content, TriviaResponse::class.java)
                    _isLoading.value = false
                }
            } catch (e: OpenAIException) {
                Log.d("OPEN_AI", e.message.toString())
                _error.value = true
                _errorMessage.value = "Couldn't generate trivia questions, please check your internet connection"
                _isLoading.value = false
            }
        }
    }

    /**
     * Checks if a given choice is the answer or not
     * If correct adds one point to the total score.
     */
    fun checkAnswer(choice: Int) {
        if(state.value[currentQuestionIndex.value].answer == choice) {
            _score.intValue++
        }
    }

    /**
     * If another question exists update question index to
     * go to the next question
     */
    fun goToNextQuestion() {
        if(currentQuestionIndex.value + 1 < state.value.size) {
            _currentQuestionIndex.intValue++
        } else {
            _gameOverState.value = true
        }
    }

    /**
     * Clear the error message
     */
    fun confirmError() {
        _errorMessage.value = ""
    }
}