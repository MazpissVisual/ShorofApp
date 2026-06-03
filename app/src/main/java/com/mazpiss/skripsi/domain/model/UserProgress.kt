package com.mazpiss.skripsi.domain.model

data class MateriProgressItem(
    val selesai: Int = 0,
    val total: Int = 0
) {
    val percent: Float get() = if (total == 0) 0f else selesai.toFloat() / total.toFloat()
}

data class UserProgress(
    val streak: Int = 0,
    val xp: Int = 0,
    val lastActiveDate: String = "",
    val materiProgress: Map<String, MateriProgressItem> = emptyMap()
) {
    val overallPercent: Float
        get() {
            if (materiProgress.isEmpty()) return 0f
            val total = materiProgress.values.sumOf { it.total }
            val selesai = materiProgress.values.sumOf { it.selesai }
            return if (total == 0) 0f else selesai.toFloat() / total.toFloat()
        }
}
