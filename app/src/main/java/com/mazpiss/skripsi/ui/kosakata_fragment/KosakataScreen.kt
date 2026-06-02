package com.mazpiss.skripsi.ui.kosakata_fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.ui.components.ShorofToolbar
import com.mazpiss.skripsi.ui.kosakata.Kosakata
import com.mazpiss.skripsi.ui.theme.ShorofTheme

@Composable
fun KosakataScreen(
    viewModel: KosakataViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    KosakataContent(uiState = uiState, modifier = modifier)
}

@Composable
private fun KosakataContent(
    uiState: KosakataUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ShorofToolbar(title = stringResource(R.string.kosakata))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            KosakataTableHeader()
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.surface)
            )

            if (uiState.isLoading) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.kosakataList) { kosakata ->
                        KosakataListRow(kosakata = kosakata)
                    }
                }
            }
        }
    }
}

@Composable
private fun KosakataTableHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        KosakataHeaderCell(
            text = stringResource(R.string.terjemahan),
            isHighlighted = true,
            modifier = Modifier.weight(1f)
        )
        KosakataHeaderCell(
            text = stringResource(R.string.latin),
            isHighlighted = false,
            modifier = Modifier.weight(1f)
        )
        KosakataHeaderCell(
            text = stringResource(R.string.arab),
            isHighlighted = true,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun KosakataHeaderCell(
    text: String,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = if (isHighlighted) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = modifier
            .background(
                if (isHighlighted) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface
            )
            .padding(vertical = 8.dp)
    )
}

@Composable
private fun KosakataListRow(
    kosakata: Kosakata,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        KosakataDataCell(
            text = kosakata.Terjemahan,
            isHighlighted = true,
            modifier = Modifier.weight(1f)
        )
        KosakataDataCell(
            text = kosakata.Latin,
            isHighlighted = false,
            modifier = Modifier.weight(1f)
        )
        KosakataDataCell(
            text = kosakata.Arab,
            isHighlighted = true,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun KosakataDataCell(
    text: String,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = if (isHighlighted) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = modifier
            .background(
                if (isHighlighted) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface
            )
            .padding(5.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun KosakataScreenPreview() {
    ShorofTheme {
        KosakataContent(
            uiState = KosakataUiState(
                kosakataList = listOf(
                    Kosakata("مَكْتَبٌ", "Maktabun", "Meja"),
                    Kosakata("كُرْسِيٌّ", "Kursiyyun", "Kursi")
                )
            )
        )
    }
}
