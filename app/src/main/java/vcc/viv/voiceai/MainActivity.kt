package vcc.viv.voiceai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import vcc.viv.voiceai.ui.screen.sale.SaleScreen
import vcc.viv.voiceai.ui.theme.VoiceAiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    //    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("onCreate MainActivity")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoiceAiTheme {
                SaleScreen()
            }
        }
        // handle error
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                mainViewModel.error.collect { error ->
//                    Timber.i("Error: $error")
//                    if(!error.isNullOrEmpty()) {
//                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
//                    }
////                    mainViewModel.clearError()
//                }
//            }
//        }
    }

    override fun onDestroy() {
        Timber.i("onDestroy MainActivity")
        super.onDestroy()
    }
}
