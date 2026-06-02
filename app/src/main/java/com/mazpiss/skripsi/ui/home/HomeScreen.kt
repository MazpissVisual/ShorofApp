package com.mazpiss.skripsi.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.ui.kosakata.Kosakata
import com.mazpiss.skripsi.ui.theme.Blue900
import com.mazpiss.skripsi.ui.theme.ShorofTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToAbout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        onNavigateToAbout = onNavigateToAbout,
        modifier = modifier
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HomeToolbar(onAboutClick = onNavigateToAbout)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 25.dp)
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = stringResource(R.string.salam),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Blue900
            )
            Text(
                text = stringResource(R.string.subSalam),
                style = MaterialTheme.typography.bodyMedium,
                color = Blue900
            )
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.mari_belajar),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.sub_mari_belajar),
                            style = MaterialTheme.typography.bodySmall,
                            color = Blue900
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.icon_learn),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.kosakata_harian),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Blue900
            )
            Text(
                text = stringResource(R.string.sub_kosakata_harian),
                style = MaterialTheme.typography.bodySmall,
                color = Blue900
            )
            Spacer(modifier = Modifier.height(20.dp))

            KosakataTable(
                kosakataList = uiState.kosakataList,
                isLoading = uiState.isLoading
            )

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
private fun HomeToolbar(
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .statusBarsPadding()
            .height(56.dp)
    ) {
        IconButton(
            onClick = onAboutClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.tentang_aplikasi),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun KosakataTable(
    kosakataList: List<Kosakata>,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.kosakata_harian),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            KosakataHeader()

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                kosakataList.forEach { kosakata ->
                    KosakataRow(kosakata = kosakata)
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
private fun KosakataHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        KosakataCell(
            text = stringResource(R.string.terjemahan),
            isHighlighted = true,
            modifier = Modifier.weight(1f)
        )
        KosakataCell(
            text = stringResource(R.string.latin),
            isHighlighted = false,
            modifier = Modifier.weight(1f)
        )
        KosakataCell(
            text = stringResource(R.string.arab),
            isHighlighted = true,
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(MaterialTheme.colorScheme.surface)
    )
}

@Composable
private fun KosakataRow(kosakata: Kosakata) {
    Row(modifier = Modifier.fillMaxWidth()) {
        KosakataCell(
            text = kosakata.Terjemahan,
            isHighlighted = true,
            modifier = Modifier.weight(1f)
        )
        KosakataCell(
            text = kosakata.Latin,
            isHighlighted = false,
            modifier = Modifier.weight(1f)
        )
        KosakataCell(
            text = kosakata.Arab,
            isHighlighted = true,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun KosakataCell(
    text: String,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(
                if (isHighlighted) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface
            )
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = if (isHighlighted) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ShorofTheme {
        HomeContent(
            uiState = HomeUiState(
                kosakataList = listOf(
                    Kosakata("مَكْتَبٌ", "Maktabun", "Meja"),
                    Kosakata("كُرْسِيٌّ", "Kursiyyun", "Kursi")
                )
            ),
            onNavigateToAbout = {}
        )
    }
}
