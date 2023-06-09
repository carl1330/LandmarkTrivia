package se.umu.cs.dv21cgn.landmarktrivia.ui.screens

/**
 * Screen constants
 */
sealed class Screen(val route: String) {
    object TriviaCardScreen: Screen("trivia_card_list_screen")
    object TriviaGameScreen: Screen("trivia_game_screen")
}