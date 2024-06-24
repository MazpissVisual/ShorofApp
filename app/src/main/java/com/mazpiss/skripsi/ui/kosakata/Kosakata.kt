package com.mazpiss.skripsi.ui.kosakata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Kosakata(
    val kosakata1: String,
    val kosakata2: String,
    val terjemahan1: String,
    val terjemahan2: String
) :Parcelable
