package com.mazpiss.skripsi.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mazpiss.skripsi.domain.model.MateriProgressItem
import com.mazpiss.skripsi.domain.model.UserProgress
import com.mazpiss.skripsi.domain.repository.ProgressRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ProgressRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProgressRepository {

    private fun userDoc(userId: String) =
        firestore.collection("userProgress").document(userId)

    override fun getUserProgress(userId: String): Flow<UserProgress> = callbackFlow {
        val listener = userDoc(userId).addSnapshotListener { snap, error ->
            if (error != null || snap == null) {
                trySend(UserProgress())
                return@addSnapshotListener
            }
            val streak = (snap.getLong("streak") ?: 0).toInt()
            val xp = (snap.getLong("xp") ?: 0).toInt()
            val lastActive = snap.getString("lastActiveDate") ?: ""

            @Suppress("UNCHECKED_CAST")
            val rawProgress = snap.get("materiProgress") as? Map<String, Map<String, Long>>
            val materiProgress = rawProgress?.mapValues { (_, v) ->
                MateriProgressItem(
                    selesai = (v["selesai"] ?: 0).toInt(),
                    total = (v["total"] ?: 0).toInt()
                )
            } ?: emptyMap()

            trySend(UserProgress(streak, xp, lastActive, materiProgress))
        }
        awaitClose { listener.remove() }
    }

    override suspend fun updateStreak(userId: String) {
        val today = today()
        val snap = userDoc(userId).get().await()
        val lastActive = snap.getString("lastActiveDate") ?: ""
        val currentStreak = (snap.getLong("streak") ?: 0).toInt()

        val newStreak = when (lastActive) {
            today -> currentStreak
            yesterday() -> currentStreak + 1
            else -> 1
        }

        userDoc(userId).set(
            mapOf("streak" to newStreak, "lastActiveDate" to today),
            SetOptions.merge()
        ).await()
    }

    override suspend fun addXp(userId: String, xp: Int) {
        val snap = userDoc(userId).get().await()
        val current = (snap.getLong("xp") ?: 0).toInt()
        userDoc(userId).set(
            mapOf("xp" to current + xp),
            SetOptions.merge()
        ).await()
    }

    override suspend fun updateMateriProgress(
        userId: String, materiId: String, selesai: Int, total: Int
    ) {
        userDoc(userId).set(
            mapOf("materiProgress" to mapOf(
                materiId to mapOf("selesai" to selesai, "total" to total)
            )),
            SetOptions.merge()
        ).await()
    }

    override suspend fun saveQuizResult(userId: String, quizId: String, score: Int, total: Int) {
        val xpGained = (score.toFloat() / total * 50).toInt()
        addXp(userId, xpGained)
        updateStreak(userId)
    }

    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private fun yesterday(): String {
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DAY_OF_YEAR, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
    }
}
