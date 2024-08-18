package com.mazpiss.skripsi.ui.materiDetail

data class MateriDetail(
    var judul: String = "",
    var subJudul: String = "",
    var content: String = "",
    var materiId: String = ""
) {
    // Konstruktor tanpa argumen diperlukan oleh Firebase Firestore
    constructor() : this("", "", "", "")
}
