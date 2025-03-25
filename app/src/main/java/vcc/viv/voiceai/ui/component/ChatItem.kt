package vcc.viv.voiceai.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import vcc.viv.voiceai.common.model.Message
import vcc.viv.voiceai.common.model.Role

//@Composable
//fun ChatItem(message: Message, modifier: Modifier = Modifier) {
//    val isModelMessage = message.participant == Role.USER.title
//
//    val messageBackgroundColor = when(message.participant) {
//        Role.USER -> MaterialTheme.colorScheme.tertiaryContainer
//        Role.ASSISTANT -> MaterialTheme.colorScheme
//    }
//}