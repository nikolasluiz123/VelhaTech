package br.com.velhatech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import br.com.velhatech.core.theme.VelhaTechTheme
import br.com.velhatech.navigation.VelhaTechNavHost
import br.com.velhatech.screen.login.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VelhaTechTheme {
                App {
                    VelhaTechNavHost(navController = rememberNavController())
                }
            }
        }
    }
}

@Composable
fun App(content: @Composable () -> Unit = { LoginScreen() }) {
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
fun AppPreview() {
    VelhaTechTheme {
        Surface {
            App()
        }
    }
}