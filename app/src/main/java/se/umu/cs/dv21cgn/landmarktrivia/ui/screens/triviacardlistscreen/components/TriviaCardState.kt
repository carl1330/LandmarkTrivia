package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviacardlistscreen.components

import android.graphics.Bitmap
import com.google.android.libraries.places.api.model.PhotoMetadata

data class TriviaCardState(
    val title: String = "",
    val description: String = "",
    val image: Bitmap? = null
)
