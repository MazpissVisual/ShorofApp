package com.mazpiss.skripsi.ui.aboutApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.ui.components.ShorofToolbar
import com.mazpiss.skripsi.ui.theme.Blue900
import com.mazpiss.skripsi.ui.theme.ShorofTheme

class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShorofTheme {
                AboutScreen(
                    onBackClick = { finish() }
                )
            }
        }
    }
}

@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ShorofToolbar(
                title = stringResource(id = R.string.tentang_aplikasi),
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.tentang_shorof),
                        style = MaterialTheme.typography.titleMedium,
                        color = Blue900,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Text(
                        text = stringResource(id = R.string.sub_mengenal_shorof),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Blue900.copy(alpha = 0.85f),
                        lineHeight = 22.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = stringResource(id = R.string.sub_mengenal_shorof_2),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Blue900.copy(alpha = 0.85f),
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    ShorofTheme {
        AboutScreen(onBackClick = {})
    }
}