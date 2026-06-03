package com.mazpiss.skripsi.ui.quizList

import androidx.lifecycle.SavedStateHandle
import com.mazpiss.skripsi.domain.repository.QuizRepository
import com.mazpiss.skripsi.ui.quiz.QuestionModel
import com.mazpiss.skripsi.ui.quiz.QuizModel
import com.mazpiss.skripsi.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: QuizRepository = mockk()

    private val dummyQuestions = listOf(
        QuestionModel("Apa itu Fi'il Madhi?", listOf("Kata kerja lampau", "Kata benda", "Kata sifat", "Partikel"), "Kata kerja lampau"),
        QuestionModel("Apa itu Isim?", listOf("Kata benda", "Kata kerja", "Kata sifat", "Kata keterangan"), "Kata benda")
    )
    private val dummyQuiz = QuizModel("1", "Quiz Shorof", "Tes Shorof", "10", dummyQuestions)

    private fun createViewModel(quizId: String? = null): QuizListViewModel {
        val handle = if (quizId != null) SavedStateHandle(mapOf("quizId" to quizId))
                     else SavedStateHandle()
        return QuizListViewModel(repository, handle)
    }

    @Test
    fun `tanpa quizId, state awal kosong dan tidak loading`() {
        val viewModel = createViewModel()
        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertTrue(state.questions.isEmpty())
        assertNull(state.error)
    }

    @Test
    fun `dengan quizId valid, questions terisi setelah load`() = runTest {
        coEvery { repository.getQuizById("1") } returns dummyQuiz
        val viewModel = createViewModel("1")

        mainDispatcherRule.testDispatcher.scheduler.runCurrent()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(dummyQuestions, state.questions)
        assertNull(state.error)
    }

    @Test
    fun `quiz tidak ditemukan menampilkan pesan error`() = runTest {
        coEvery { repository.getQuizById("99") } returns null
        val viewModel = createViewModel("99")

        mainDispatcherRule.testDispatcher.scheduler.runCurrent()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
    }

    @Test
    fun `repository gagal menampilkan pesan error`() = runTest {
        coEvery { repository.getQuizById("1") } throws RuntimeException("Network error")
        val viewModel = createViewModel("1")

        mainDispatcherRule.testDispatcher.scheduler.runCurrent()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
    }

    @Test
    fun `selectAnswer memperbarui selectedAnswer di state`() = runTest {
        coEvery { repository.getQuizById("1") } returns dummyQuiz
        val viewModel = createViewModel("1")
        mainDispatcherRule.testDispatcher.scheduler.runCurrent()

        viewModel.selectAnswer("Kata kerja lampau")

        assertEquals("Kata kerja lampau", viewModel.uiState.value.selectedAnswer)
    }

    @Test
    fun `nextQuestion tanpa jawaban mengembalikan false`() = runTest {
        coEvery { repository.getQuizById("1") } returns dummyQuiz
        val viewModel = createViewModel("1")
        mainDispatcherRule.testDispatcher.scheduler.runCurrent()

        val result = viewModel.nextQuestion()

        assertFalse(result)
        assertEquals(0, viewModel.uiState.value.currentIndex)
    }

    @Test
    fun `nextQuestion dengan jawaban benar menambah skor`() = runTest {
        coEvery { repository.getQuizById("1") } returns dummyQuiz
        val viewModel = createViewModel("1")
        mainDispatcherRule.testDispatcher.scheduler.runCurrent()
        viewModel.selectAnswer("Kata kerja lampau")

        val result = viewModel.nextQuestion()

        assertTrue(result)
        assertEquals(1, viewModel.uiState.value.score)
        assertEquals(1, viewModel.uiState.value.currentIndex)
    }

    @Test
    fun `nextQuestion dengan jawaban salah tidak menambah skor`() = runTest {
        coEvery { repository.getQuizById("1") } returns dummyQuiz
        val viewModel = createViewModel("1")
        mainDispatcherRule.testDispatcher.scheduler.runCurrent()
        viewModel.selectAnswer("Kata benda")

        viewModel.nextQuestion()

        assertEquals(0, viewModel.uiState.value.score)
    }

    @Test
    fun `nextQuestion di soal terakhir meng-set isFinished true`() = runTest {
        coEvery { repository.getQuizById("1") } returns dummyQuiz
        val viewModel = createViewModel("1")
        mainDispatcherRule.testDispatcher.scheduler.runCurrent()

        viewModel.selectAnswer("Kata kerja lampau")
        viewModel.nextQuestion()
        viewModel.selectAnswer("Kata benda")
        viewModel.nextQuestion()

        assertTrue(viewModel.uiState.value.isFinished)
    }

    @Test
    fun `timer habis meng-set isFinished true`() {
        coEvery { repository.getQuizById("1") } returns dummyQuiz.copy(time = "1")
        val viewModel = createViewModel("1")

        mainDispatcherRule.testDispatcher.scheduler.advanceTimeBy(62_000L)
        mainDispatcherRule.testDispatcher.scheduler.runCurrent()

        assertTrue(viewModel.uiState.value.isFinished)
    }
}
