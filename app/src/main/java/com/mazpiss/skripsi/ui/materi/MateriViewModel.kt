package com.mazpiss.skripsi.ui.materi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject

class MateriViewModel : ViewModel() {

    private val _materiList = MutableLiveData<List<Materi>>()
    val materiList: LiveData<List<Materi>> get() = _materiList
    private val db = FirebaseFirestore.getInstance()

    init {
        fetchMateriData()
    }

    private fun fetchMateriData() {
        db.collection("Materi").orderBy("totalPembahasan",Query.Direction.ASCENDING)
            .addSnapshotListener(object :EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error !=null){
                        return
                    }
                    val materiArrayList = arrayListOf<Materi>()
                    for (dc:DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            materiArrayList.add(dc.document.toObject(Materi::class.java))
                        }
                    }
                    _materiList.value = materiArrayList
                }

            })
    }
}