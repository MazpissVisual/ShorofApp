package com.mazpiss.skripsi.ui.tentang

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TentangViewModel : ViewModel() {

    private val _appDescription  = MutableLiveData<String>().apply {
        value = "Deskripsi aplikasi"
    }
    val appDescription:LiveData<String> = _appDescription
    private val _developerDescription = MutableLiveData<String>().apply {
        value = "Aplikasi Shorof Learn merupkan aplikasi yang dibuat sebagai media pembelajaran ilmu Shorof secara dasar. Aplikasi ini memfasilitasi kebutuhan media pembelajaran bagi mahasiswa UNIDA  Gontor dan masyarakat umum."
    }
    val developerDescription: LiveData<String> = _developerDescription
}