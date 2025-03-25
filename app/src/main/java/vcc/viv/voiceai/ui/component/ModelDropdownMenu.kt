package vcc.viv.voiceai.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vcc.viv.voiceai.common.model.ModelInfo

@Composable
fun ModelDropdownMenu(
    models: List<ModelInfo>,
    modifier: Modifier = Modifier,
    onClickedModel: (modelInfo: ModelInfo) -> Unit = {}
) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(8.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            models.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.id) },
                    onClick = {
                        onClickedModel(option)
                        expanded = !expanded
                    }

                )
            }
        }
    }
}