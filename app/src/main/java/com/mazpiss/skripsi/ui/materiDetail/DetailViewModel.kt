package com.mazpiss.skripsi.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.mazpiss.skripsi.ui.materi.Materi
import com.mazpiss.skripsi.ui.materiDetail.MateriDetail

class DetailViewModel : ViewModel() {

    private val _subMateriList = MutableLiveData<List<MateriDetail>>()
    val subMateriList: LiveData<List<MateriDetail>> get() = _subMateriList

    private val db = FirebaseFirestore.getInstance()

    fun loadSubMateri(judul: String?) {

        db.collection("subMateri").whereEqualTo("judul", judul).get()
            .addOnSuccessListener { documents ->
                val subMateriArrayList = arrayListOf<MateriDetail>()
                for (document in documents) {
                    subMateriArrayList.add(document.toObject(MateriDetail::class.java))
                }
                val sortedList = subMateriArrayList.sortedBy { it.subJudul }
                _subMateriList.value = sortedList
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }
}
