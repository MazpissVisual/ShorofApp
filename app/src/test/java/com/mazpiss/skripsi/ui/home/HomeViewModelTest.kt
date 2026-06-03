package com.mazpiss.skripsi.ui.home

import app.cash.turbine.test
import com.mazpiss.skripsi.domain.repository.KosakataRepository
import com.mazpiss.skripsi.ui.kosakata.Kosakata
import com.mazpiss.skripsi.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: KosakataRepository = mockk()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        every { repository.getKosakata() } returns flowOf(emptyList())
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `state awal isLoading false dan list kosong`() = runTest {
        every { repository.getKosakata() } returns flowOf(emptyList())
        viewModel = HomeViewModel(repository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.kosakataList.isEmpty())
    }

    @Test
    fun `ketika repository emit data, uiState berisi daftar kosakata`() = runTest {
        val dummyList = listOf(
            Kosakata("مَكْتَبٌ", "Maktabun", "Meja"),
            Kosakata("كُرْسِيٌّ", "Kursiyyun", "Kursi")
        )
        every { repository.getKosakata() } returns flowOf(dummyList)

        viewModel = HomeViewModel(repository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(dummyList, state.kosakataList)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ketika repository error, isLoading kembali false`() = runTest {
        every { repository.getKosakata() } returns kotlinx.coroutines.flow.flow {
            throw RuntimeException("Firestore error")
        }

        viewModel = HomeViewModel(repository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
    }
}
