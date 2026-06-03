package com.mazpiss.skripsi.domain.repository

import com.mazpiss.skripsi.domain.model.UserProgress
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun getUserProgress(userId: String): Flow<UserProgress>
    suspend fun updateStreak(userId: String)
    suspend fun addXp(userId: String, xp: Int)
    suspend fun updateMateriProgress(userId: String, materiId: String, selesai: Int, total: Int)
    suspend fun saveQuizResult(userId: String, quizId: String, score: Int, total: Int)
}
