package com.mazpiss.skripsi.ui.kosakata


data class Kosakata(
    var Arab: String = "",
    var Latin: String = "",
    var Terjemahan: String = "",
) {
    // Konstruktor tanpa argumen
    constructor() : this("", "", "")
}
