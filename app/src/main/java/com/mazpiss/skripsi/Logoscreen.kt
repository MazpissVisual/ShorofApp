package com.mazpiss.skripsi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mazpiss.skripsi.ui.theme.Primary
import com.mazpiss.skripsi.ui.theme.ShorofTheme
import kotlinx.coroutines.delay

@AndroidEntryPoint
class Logoscreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShorofTheme {
                SplashScreenContent(
                    onTimeout = {
                        val intent = Intent(this@Logoscreen, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun SplashScreenContent(
    onTimeout: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Primary)
    ) {
        // Motif background di bagian bawah layar
        Image(
            painter = painterResource(id = R.drawable.motifbawah2),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(206.dp)
                .align(Alignment.BottomCenter)
        )

        // Logo aplikasi di bagian tengah
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Shorof Learn",
            modifier = Modifier
                .width(278.dp)
                .height(96.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    ShorofTheme {
        SplashScreenContent(onTimeout = {})
    }
}
