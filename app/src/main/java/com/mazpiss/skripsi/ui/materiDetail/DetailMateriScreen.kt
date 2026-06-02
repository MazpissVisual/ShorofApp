package com.mazpiss.skripsi.ui.materiDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.ui.components.ShorofToolbar
import com.mazpiss.skripsi.ui.detail.DetailUiState
import com.mazpiss.skripsi.ui.detail.DetailViewModel
import com.mazpiss.skripsi.ui.theme.Blue900
import com.mazpiss.skripsi.ui.theme.ShorofTheme

@Composable
fun DetailMateriScreen(
    materiJudul: String?,
    viewModel: DetailViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onSubMateriClick: (MateriDetail) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LaunchedEffect(materiJudul) {
        viewModel.loadSubMateri(materiJudul)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetailMateriContent(
        judul = materiJudul ?: "",
        uiState = uiState,
        onBackClick = onBackClick,
        onSubMateriClick = onSubMateriClick,
        modifier = modifier
    )
}

@Composable
private fun DetailMateriContent(
    judul: String,
    uiState: DetailUiState,
    onBackClick: () -> Unit,
    onSubMateriClick: (MateriDetail) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ShorofToolbar(
                title = judul,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(horizontal = 20.dp, vertical = 30.dp),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_search),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }

            Text(
                text = stringResource(R.string.sub_materi),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Text(
                text = stringResource(R.string.alur_pembelajaran),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Blue900,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp)
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.subMateriList) { subMateri ->
                        SubMateriItem(
                            subMateri = subMateri,
                            onClick = { onSubMateriClick(subMateri) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SubMateriItem(
    subMateri: MateriDetail,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = subMateri.subJudul,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailMateriScreenPreview() {
    ShorofTheme {
        DetailMateriContent(
            judul = "Fi'il Madhi",
            uiState = DetailUiState(
                subMateriList = listOf(
                    MateriDetail("Fi'il Madhi", "Pengertian Fi'il Madhi", "Konten...", "1"),
                    MateriDetail("Fi'il Madhi", "Tashrifan Fi'il Madhi", "Konten...", "2")
                )
            ),
            onBackClick = {},
            onSubMateriClick = {}
        )
    }
}
