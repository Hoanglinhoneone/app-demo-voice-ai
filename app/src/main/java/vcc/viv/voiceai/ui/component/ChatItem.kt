package vcc.viv.voiceai.ui.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vcc.viv.voiceai.common.model.Message
import vcc.viv.voiceai.common.model.Participant
import vcc.viv.voiceai.common.model.Role

@Composable
fun ChatItem(message: Message, modifier: Modifier = Modifier) {
    val isUserMessage = message.participant == Role.USER.title

    val messageBackgroundColor = when (message.participant) {
        Role.USER.title -> MaterialTheme.colorScheme.tertiaryContainer
        Role.ASSISTANT.title -> MaterialTheme.colorScheme.primaryContainer
        else -> {
            MaterialTheme.colorScheme.errorContainer
        }
    }

    val bubbleShape = if (isUserMessage) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }

    val alignment = if (isUserMessage) Alignment.End else Alignment.Start

    Column(
        horizontalAlignment = alignment,
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()
    ) {

        BoxWithConstraints {
            Card(
                colors = CardDefaults.cardColors(containerColor = messageBackgroundColor),
                shape = bubbleShape,
                modifier = Modifier.widthIn(0.dp, maxWidth * 0.9f)
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
        Text(text =  if(isUserMessage) Participant.USER.title else Participant.ASSISTANT.title,
            color = MaterialTheme.colorScheme.outline)
    }
}

@Preview(showBackground = true)
@Composable
fun ChatItemPreview() {
    Surface {
        ChatItem(
            message = Message(
                participant = Role.USER.title,
                content = "Hoàng ngọc linh nè"
            )
        )
    }
}
