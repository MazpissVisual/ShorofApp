package com.mazpiss.skripsi.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mazpiss.skripsi.domain.repository.MateriRepository
import com.mazpiss.skripsi.ui.materi.Materi
import com.mazpiss.skripsi.ui.materiDetail.MateriDetail
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MateriRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : MateriRepository {

    override fun getMateri(): Flow<List<Materi>> = callbackFlow {
        val listener = db.collection("Materi")
            .orderBy("totalPembahasan", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.documents
                    ?.mapNotNull { it.toObject(Materi::class.java) }
                    ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    override fun getSubMateri(judul: String): Flow<List<MateriDetail>> = callbackFlow {
        val listener = db.collection("subMateri")
            .whereEqualTo("judul", judul)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.documents
                    ?.mapNotNull { it.toObject(MateriDetail::class.java) }
                    ?.sortedBy { it.subJudul }
                    ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }
}
