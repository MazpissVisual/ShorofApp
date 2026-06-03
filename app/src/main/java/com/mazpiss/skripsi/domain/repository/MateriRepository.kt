package com.mazpiss.skripsi.domain.repository

import com.mazpiss.skripsi.ui.materi.Materi
import com.mazpiss.skripsi.ui.materiDetail.MateriDetail
import kotlinx.coroutines.flow.Flow

interface MateriRepository {
    fun getMateri(): Flow<List<Materi>>
    fun getSubMateri(judul: String): Flow<List<MateriDetail>>
}
