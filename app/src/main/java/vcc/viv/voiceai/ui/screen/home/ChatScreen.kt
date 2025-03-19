package vcc.viv.voiceai.ui.screen.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import vcc.viv.voiceai.MainViewModel
import vcc.viv.voiceai.R
import vcc.viv.voiceai.common.Constans
import vcc.viv.voiceai.common.model.Message
import vcc.viv.voiceai.ui.theme.VoiceAiTheme

@SuppressLint("ShowToast")
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val chatViewModel: ChatViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val ttsState = mainViewModel.ttsState.collectAsState()
    val sttState = mainViewModel.sttState.collectAsState()
    val uiState = chatViewModel.uiState.collectAsState()
    val messages by chatViewModel.messages.collectAsState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                Timber.i("Permission granted = $isGranted")
                if (isGranted) {
                    mainViewModel.startListening()
                }
            })
    LaunchedEffect(ttsState.value.isSpeaking) {
        Toast.makeText(context, "Bắt đầu đọc", Toast.LENGTH_SHORT).show()
    }
    LaunchedEffect(sttState.value.error) {
        Toast.makeText(context, "${sttState.value.error}", Toast.LENGTH_SHORT).show()
    }
    LaunchedEffect(sttState.value.spokenText) {
        chatViewModel.updateMessageInput(sttState.value.spokenText)
    }
    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ){ paddingValues ->
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.LightGray)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .weight(1f)
                        .fillMaxSize()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .align(Alignment.CenterHorizontally)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cloud),
                            contentDescription = null,
                            modifier = Modifier.clickable {
//                                chatViewModel.updateMessageInput("Hello Hoang Ngoc Linh")
                                Timber.i("Permission handle")
                                when {
                                    ContextCompat.checkSelfPermission(
                                        context, Manifest.permission.RECORD_AUDIO
                                    ) == PackageManager.PERMISSION_GRANTED -> {
                                        Timber.i("Permission already granted")
                                        mainViewModel.startListening()
                                    }

                                    ActivityCompat.shouldShowRequestPermissionRationale(
                                        context as Activity, Manifest.permission.RECORD_AUDIO
                                    ) -> {
                                        Timber.i("Should show a permission rationale")
                                        scope.launch {
                                            snackBarHostState.showSnackbar("Vui lòng mở cài đặt và cấp quyền micro")
                                        }
                                    }

                                    else -> {
                                        Timber.i("Request record audio permission")
                                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    }
                                }
                            }
                        )
                        Text(
                            text = "Text to Speech",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .align(Alignment.CenterHorizontally)

                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                mainViewModel.updateRoleSpeak(Constans.Role.SYSTEM)
                                mainViewModel.speak("Xin chào Hoàng ngọc linh nhé")
                                chatViewModel.updateMessageOutput("Xin chào Hoàng ngọc linh nhé")
                            }
                        )
                        Text(
                            text = "Speech to Text",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 8.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .background(Color.LightGray)
                    .padding(8.dp)
            ) {
                ChatView(messages)
            }
        }
    }
}

@Composable
fun ItemChat(
    message: Message,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMe) Arrangement.End else Arrangement.Start
    ) {
        Text(
            text = message.text,
            modifier = modifier
                .background(
                    if (message.isMe) Color.LightGray else Color.Gray,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(10.dp, 10.dp)

        )
    }
}

@Composable
fun ChatView(
    messages: List<Message> = listOf(
        Message("Hello chat", true),
        Message("Hello Linh", false),
        Message("How are you", true),
        Message("I'm fine, thank you", false),
        Message("What's your name", true),
        Message("My name is ChatGpt", false),
        Message("Nice to meet you", true),
        Message("Bye", false)
    ),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Blue, shape = RoundedCornerShape(16.dp)),
        contentPadding = PaddingValues(8.dp, 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(messages) { message ->
            ItemChat(message)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    VoiceAiTheme {
        ChatScreen()
    }
}