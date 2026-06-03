package com.mazpiss.skripsi.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun register(name: String, email: String, password: String): Result<FirebaseUser>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser>
    fun logout()
}
