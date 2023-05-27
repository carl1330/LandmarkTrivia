package se.umu.cs.dv21cgn.landmarktrivia.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun TriviaCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onClick: () -> Unit,
    imageUrl: String = "",
    isFetching: Boolean
) {
    Card(
        modifier.fillMaxWidth()
    ) {
        if(isFetching) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                modifier = modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
        Column(Modifier.padding(8.dp)) {
            Column {
                Text(text = title, fontSize = 20.sp)
                Text(text = description)
            }
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier.fillMaxWidth()
            ){
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Outlined.Info, contentDescription = "")
                }
                Button(onClick = onClick) {
                    Text(text = "Start")
                }
            }
        }
    }
}

@Composable
@Preview
fun TriviaCardPreview() {
    TriviaCard(
        title = "Umeå",
        description = "Short description about Umeå",
        onClick = {},
        imageUrl = "null",
        isFetching = false
    )
}