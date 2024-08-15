package com.mazpiss.skripsi.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class QuizViewModel:ViewModel() {
    private val _quizModelList = MutableLiveData<List<QuizModel>>()
    val quizModelList: LiveData<List<QuizModel>> get() = _quizModelList

    private val _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean>get() = _loading

    private val _error = MutableLiveData<String>()
    val error : LiveData<String>get() = _error

    init {
        getDataFromFirebase()
    }

    private fun getDataFromFirebase() {
        _loading.value = true
        FirebaseDatabase.getInstance().reference.child("quizzes")
            .get()
            .addOnSuccessListener { dataSpapshot->
                if (dataSpapshot.exists()){
                    val tempList = mutableListOf<QuizModel>()
                    for (snapshot in dataSpapshot.children){
                        try {
                            val quizModel = snapshot.getValue(QuizModel::class.java)
                            if (quizModel != null){
                                tempList.add(quizModel)
                            }
                        }catch (e:DatabaseException){
                            _error.value = "Failed To Convert Value"
                        }
                    }
                    _quizModelList.value = tempList
                }
                _loading.value = false
            }
            .addOnFailureListener{exception->
                _error.value = "error getting data"
                _loading.value = false
            }
    }
}