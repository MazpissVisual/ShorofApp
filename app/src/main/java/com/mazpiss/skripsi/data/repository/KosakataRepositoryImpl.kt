package com.mazpiss.skripsi.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mazpiss.skripsi.domain.repository.KosakataRepository
import com.mazpiss.skripsi.ui.kosakata.Kosakata
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KosakataRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : KosakataRepository {

    override fun getKosakata(): Flow<List<Kosakata>> = callbackFlow {
        val listener = db.collection("Kosakata")
            .orderBy("Terjemahan", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.documents
                    ?.mapNotNull { it.toObject(Kosakata::class.java) }
                    ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }
}
