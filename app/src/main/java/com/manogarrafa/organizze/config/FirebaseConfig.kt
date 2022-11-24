package com.manogarrafa.organizze.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.manogarrafa.organizze.utils.ConverterBase64

class FirebaseConfig {
    companion object {
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var firebaseDatabase: DatabaseReference
        fun getFirebaseAuth(): FirebaseAuth {
            firebaseAuth = FirebaseAuth.getInstance()
            return firebaseAuth
        }

        fun getFirebaseDatabase(): DatabaseReference {
            firebaseDatabase = FirebaseDatabase.getInstance().reference
            return firebaseDatabase
        }

        fun getUserDatabase(path: String): DatabaseReference {
            return FirebaseDatabase.getInstance().reference.child(path)
                .child(ConverterBase64.encode(firebaseAuth.currentUser?.email ?: ""))
        }
    }
}