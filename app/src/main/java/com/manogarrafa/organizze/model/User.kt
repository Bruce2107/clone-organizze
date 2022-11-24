package com.manogarrafa.organizze.model

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.manogarrafa.organizze.config.FirebaseConfig

data class User(
    @Exclude @set:Exclude @get:Exclude var id: String = "",
    var name: String? = "",
    var email: String = "",
    @Exclude @get:Exclude var password: String = "",
    var receiptTotal: Double = 0.0,
    var expensesTotal: Double = 0.0
) {


    fun salvar() {
        val firebase: DatabaseReference = FirebaseConfig.getFirebaseDatabase()
        firebase.child("usuarios").child(this.id).setValue(this)
    }
}