package com.mazpiss.skripsi.domain.repository

import com.mazpiss.skripsi.ui.quiz.QuizModel

interface QuizRepository {
    suspend fun getQuizList(): List<QuizModel>
    suspend fun getQuizById(id: String): QuizModel?
}
