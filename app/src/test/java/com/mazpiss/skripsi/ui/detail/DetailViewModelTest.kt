package com.mazpiss.skripsi.ui.detail

import app.cash.turbine.test
import com.mazpiss.skripsi.domain.repository.MateriRepository
import com.mazpiss.skripsi.ui.materiDetail.DetailUiState
import com.mazpiss.skripsi.ui.materiDetail.DetailViewModel
import com.mazpiss.skripsi.ui.materiDetail.MateriDetail
import com.mazpiss.skripsi.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: MateriRepository = mockk()
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setup() {
        every { repository.getMateri() } returns flowOf(emptyList())
        every { repository.getSubMateri(any()) } returns flowOf(emptyList())
        viewModel = DetailViewModel(repository)
    }

    @Test
    fun `loadSubMateri dengan judul null tidak memanggil repository`() = runTest {
        viewModel.loadSubMateri(null)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        verify(exactly = 0) { repository.getSubMateri(any()) }
        assertTrue(viewModel.uiState.value.subMateriList.isEmpty())
    }

    @Test
    fun `loadSubMateri dengan judul valid mengembalikan daftar sub-materi`() = runTest {
        val judul = "Fi'il Madhi"
        val dummyList = listOf(
            MateriDetail(judul, "Pengertian Fi'il Madhi", "Konten A", "1"),
            MateriDetail(judul, "Tashrifan Fi'il Madhi", "Konten B", "2")
        )
        every { repository.getSubMateri(judul) } returns flowOf(dummyList)

        viewModel.loadSubMateri(judul)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(dummyList, state.subMateriList)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadSubMateri dengan string kosong tidak memanggil repository`() = runTest {
        viewModel.loadSubMateri("")
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        verify(exactly = 0) { repository.getSubMateri(any()) }
    }
}
