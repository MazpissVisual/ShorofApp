package com.mazpiss.skripsi.ui.materi

import app.cash.turbine.test
import com.mazpiss.skripsi.domain.repository.MateriRepository
import com.mazpiss.skripsi.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MateriViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: MateriRepository = mockk()
    private lateinit var viewModel: MateriViewModel

    @Before
    fun setup() {
        every { repository.getMateri() } returns flowOf(emptyList())
        every { repository.getSubMateri(any()) } returns flowOf(emptyList())
        viewModel = MateriViewModel(repository)
    }

    @Test
    fun `state awal isLoading false dan list kosong`() = runTest {
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assert(state.materiList.isEmpty())
    }

    @Test
    fun `ketika repository emit data, daftar materi tampil`() = runTest {
        val dummyMateri = listOf(
            Materi("Fi'il Madhi", "1", "3 Pembahasan"),
            Materi("Fi'il Mudhari", "2", "5 Pembahasan")
        )
        every { repository.getMateri() } returns flowOf(dummyMateri)
        viewModel = MateriViewModel(repository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(dummyMateri, state.materiList)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ketika repository error, isLoading kembali false`() = runTest {
        every { repository.getMateri() } returns kotlinx.coroutines.flow.flow {
            throw RuntimeException("Network error")
        }
        viewModel = MateriViewModel(repository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
    }
}
