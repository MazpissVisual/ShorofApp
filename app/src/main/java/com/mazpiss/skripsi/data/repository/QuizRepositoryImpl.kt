package com.mazpiss.skripsi.data.repository

import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase
import com.mazpiss.skripsi.domain.repository.QuizRepository
import com.mazpiss.skripsi.ui.quiz.QuizModel
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class QuizRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : QuizRepository {

    override suspend fun getQuizList(): List<QuizModel> = suspendCancellableCoroutine { cont ->
        val task = database.reference.child("quizzes").get()
        task.addOnSuccessListener { snapshot ->
            if (!cont.isActive) return@addOnSuccessListener
            val list = mutableListOf<QuizModel>()
            for (child in snapshot.children) {
                try {
                    // Gunakan child.key sebagai id karena field 'id' tidak disimpan di dokumen Firebase
                    child.getValue(QuizModel::class.java)?.let { model ->
                        list.add(model.copy(id = child.key ?: model.id))
                    }
                } catch (_: DatabaseException) { }
            }
            cont.resume(list)
        }
        task.addOnFailureListener { error ->
            if (cont.isActive) cont.resumeWithException(error)
        }
    }

    override suspend fun getQuizById(id: String): QuizModel? = suspendCancellableCoroutine { cont ->
        val task = database.reference.child("quizzes").child(id).get()
        task.addOnSuccessListener { snapshot ->
            if (!cont.isActive) return@addOnSuccessListener
            val quiz = try {
                snapshot.getValue(QuizModel::class.java)?.copy(id = snapshot.key ?: "")
            } catch (_: DatabaseException) { null }
            cont.resume(quiz)
        }
        task.addOnFailureListener { error ->
            if (cont.isActive) cont.resumeWithException(error)
        }
    }
}
