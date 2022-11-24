package com.manogarrafa.organizze.model

import com.google.firebase.database.Exclude
import com.manogarrafa.organizze.config.FirebaseConfig
import com.manogarrafa.organizze.utils.CustomDate

data class Movimentacao(
    var date: String = "",
    var category: String = "",
    var description: String = "",
    var type: String = "",
    var value: Double = 0.0,
    @Exclude @get:Exclude var key: String = ""
) {
    fun salvar() {
        FirebaseConfig.getUserDatabase("movimentacao")
            .child(CustomDate.formatDate(this.date))
            .push().setValue(this)
    }


}