package com.mazpiss.skripsi.ui.kosakata_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mazpiss.skripsi.ui.kosakata.Kosakata

class KosakataViewModel : ViewModel() {
    private val dbKosakata = FirebaseFirestore.getInstance()
    private val _kosakataList = MutableLiveData<List<Kosakata>>()
    val kosakataList: LiveData<List<Kosakata>> = _kosakataList
    init {
        fetchKosakata()
    }

    private fun fetchKosakata() {
        dbKosakata.collection("Kosakata").orderBy("Terjemahan",Query.Direction.ASCENDING)
            .addSnapshotListener{value,error ->
                if (error != null){
                    return@addSnapshotListener
                }
                val list = mutableListOf<Kosakata>()
                value?.documents?.forEach{document ->
                    val kosakata = document.toObject(Kosakata::class.java)
                    kosakata?.let { list.add(it) }
                }
                _kosakataList.value = list
            }
    }
}