package vcc.viv.voiceai

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import vcc.viv.voiceai.ui.screen.sale.SaleScreen
import vcc.viv.voiceai.ui.theme.VoiceAiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val mainViewModel: MainViewModel by viewModels() // Khởi tạo ViewModel trong Activity
    private val scope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("onCreate MainActivity")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContent {
            VoiceAiTheme {
                SaleScreen()
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Timber.i("Permission granted")
                mainViewModel.startListening()
            } else {
                Timber.i("Permission denied")
            }
        }
        checkAudioPermission()
    }

    private fun checkAudioPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Nếu quyền đã được cấp
                Timber.i("Permission already granted")
                mainViewModel.startListening()  // Gọi phương thức từ ViewModel
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.RECORD_AUDIO
            ) -> {
                // Nếu cần hiển thị lý do yêu cầu quyền
                Timber.i("Should show a permission rationale")
                // Bạn có thể hiển thị một snackbar hoặc thông báo yêu cầu người dùng cấp quyền
                scope.launch {
                    Toast.makeText(this@MainActivity, "Vui lòng mở cài đặt và cấp quyền micro", Toast.LENGTH_SHORT).show()
                }
            }

            else -> {
                // Nếu chưa có quyền, yêu cầu quyền
                Timber.i("Request record audio permission")
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    override fun onDestroy() {
        Timber.i("onDestroy MainActivity")
        super.onDestroy()
    }
}
