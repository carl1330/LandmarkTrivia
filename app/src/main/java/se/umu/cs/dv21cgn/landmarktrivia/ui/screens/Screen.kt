package se.umu.cs.dv21cgn.landmarktrivia.ui.screens

sealed class Screen(val route: String) {
    object TriviaCardScreen: Screen("trivia_card_list_screen")
}