package com.mazpiss.skripsi.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mazpiss.skripsi.ui.kosakata.Kosakata
import com.mazpiss.skripsi.ui.theme.ShorofTheme
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeContent_loading_menampilkan_indikator() {
        composeTestRule.setContent {
            ShorofTheme {
                HomeContent(
                    uiState = HomeUiState(isLoading = true),
                    onNavigateToAbout = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Arab", ignoreCase = true)
            .assertIsDisplayed()
    }

    @Test
    fun homeContent_dengan_data_menampilkan_kosakata() {
        val kosakata = listOf(
            Kosakata("مَكْتَبٌ", "Maktabun", "Meja"),
            Kosakata("كُرْسِيٌّ", "Kursiyyun", "Kursi")
        )

        composeTestRule.setContent {
            ShorofTheme {
                HomeContent(
                    uiState = HomeUiState(kosakataList = kosakata),
                    onNavigateToAbout = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Meja").assertIsDisplayed()
        composeTestRule.onNodeWithText("Maktabun").assertIsDisplayed()
        composeTestRule.onNodeWithText("Kursi").assertIsDisplayed()
    }
}
