package com.mazpiss.skripsi.ui.quiz

import app.cash.turbine.test
import com.mazpiss.skripsi.domain.repository.QuizRepository
import com.mazpiss.skripsi.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class QuizViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: QuizRepository = mockk()

    @Test
    fun `state awal isLoading false dan list kosong`() = runTest {
        coEvery { repository.getQuizList() } returns emptyList()
        val viewModel = QuizViewModel(repository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.quizList.isEmpty())
    }

    @Test
    fun `ketika repository berhasil, daftar quiz tampil`() = runTest {
        val dummyQuizList = listOf(
            QuizModel("1", "Quiz Fi'il", "Tes Fi'il Madhi", "10", emptyList()),
            QuizModel("2", "Quiz Isim", "Tes Isim Fa'il", "15", emptyList())
        )
        coEvery { repository.getQuizList() } returns dummyQuizList
        val viewModel = QuizViewModel(repository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2, state.quizList.size)
            assertEquals("Quiz Fi'il", state.quizList[0].title)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ketika repository gagal, error message diset`() = runTest {
        coEvery { repository.getQuizList() } throws RuntimeException("Network error")
        val viewModel = QuizViewModel(repository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
    }
}
