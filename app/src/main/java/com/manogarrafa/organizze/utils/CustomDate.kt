package com.manogarrafa.organizze.utils

import java.text.SimpleDateFormat
import java.util.Locale


class CustomDate {

    companion object {
        fun getCurrentDate(): String {
            val date = System.currentTimeMillis()
            val format: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
            return format.format(date)
        }

        fun formatDate(data: String): String {
            return data.split("/").let {
                "${it[1]}${it[2]}"
            }
        }
    }
}