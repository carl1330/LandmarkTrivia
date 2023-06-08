package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.components

import android.graphics.Bitmap

data class TriviaCardState(
    val title: String = "",
    val description: String = "",
    val image: Bitmap? = null,
    val loading: Boolean = false,
)
