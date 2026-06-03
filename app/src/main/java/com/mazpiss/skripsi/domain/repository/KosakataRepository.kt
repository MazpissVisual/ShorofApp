package com.mazpiss.skripsi.domain.repository

import com.mazpiss.skripsi.ui.kosakata.Kosakata
import kotlinx.coroutines.flow.Flow

interface KosakataRepository {
    fun getKosakata(): Flow<List<Kosakata>>
}
