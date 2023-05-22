package se.umu.cs.dv21cgn.landmarktrivia.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.umu.cs.dv21cgn.landmarktrivia.R

@Composable
fun TriviaCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            modifier = modifier.fillMaxWidth()
                .height(120.dp),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
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
                Icon(Icons.Outlined.Info, null)
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
    TriviaCard(title = "Umeå", description = "Short description about Umeå", onClick = {})
}