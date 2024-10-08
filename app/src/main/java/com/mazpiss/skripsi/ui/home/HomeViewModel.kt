package com.mazpiss.skripsi.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mazpiss.skripsi.ui.kosakata.Kosakata

class HomeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _kosakataList = MutableLiveData<List<Kosakata>>()
    val kosakataList: LiveData<List<Kosakata>> = _kosakataList

    init {
        fetchKosakata()
    }

    private fun fetchKosakata() {
        db.collection("Kosakata").orderBy("Terjemahan",Query.Direction.ASCENDING)
//            .limit(5) // Batasi jumlah data yang diambil menjadi 4
            .addSnapshotListener { value, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                val list = mutableListOf<Kosakata>()
                value?.documents?.forEach { document ->
                    val kosakata = document.toObject(Kosakata::class.java)
                    kosakata?.let { list.add(it) }
                }
                _kosakataList.value = list
            }
    }
}
