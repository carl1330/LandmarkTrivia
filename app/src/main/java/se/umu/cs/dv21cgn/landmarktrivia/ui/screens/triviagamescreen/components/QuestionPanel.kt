package se.umu.cs.dv21cgn.landmarktrivia.ui.screens.triviagamescreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun QuestionPanel(
    question: String,
    choices: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit

) {
    Card(
        Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            Text(text = question)
            Column(
                modifier = Modifier.selectableGroup()
            ) {
                choices.forEach { text ->
                    RadioButtonText(
                        selected = (text == selectedOption),
                        text = text,
                        onClick = { onOptionSelected(text) }
                    )
                }
            }
        }
    }
}

@Composable
fun RadioButtonText(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = text)
    }
}