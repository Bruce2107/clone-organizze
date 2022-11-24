package com.manogarrafa.organizze.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.manogarrafa.organizze.commons.TypeMovimentacao
import com.manogarrafa.organizze.config.FirebaseConfig
import com.manogarrafa.organizze.databinding.ActivityReceitasBinding
import com.manogarrafa.organizze.model.Movimentacao
import com.manogarrafa.organizze.model.User
import com.manogarrafa.organizze.utils.CustomDate
import kotlin.properties.Delegates

class ReceitasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReceitasBinding
    private var receiptTotal by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceitasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        retrieveTotal()
    }

    private fun setupView() = with(binding) {
        receiptDate.setText(CustomDate.getCurrentDate())
        floatingActionButton.setOnClickListener {
            savereceipt()
        }
    }

    private fun savereceipt() {
        with(binding) {
            if (validateSaveAction()) {
                val value = receiptValue.text.toString().toDouble()
                val description = receiptDescription.text.toString()
                val date = receiptDate.text.toString()
                val category = receiptCategory.text.toString()
                Movimentacao(
                    value = value,
                    description = description,
                    date = date,
                    category = category,
                    type = TypeMovimentacao.Receipt.toString()
                ).salvar()
                updateTotal()
                finish()
            }

        }
    }

    private fun retrieveTotal() {
        FirebaseConfig.getUserDatabase("usuarios")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue<User>()
                    receiptTotal = user?.receiptTotal ?: 0.0
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value

                }
            })
    }

    private fun updateTotal() {
        val value = binding.receiptValue.text.toString().toDouble()
        val newReceipt = value + receiptTotal
        FirebaseConfig.getUserDatabase("usuarios")
            .child("receiptTotal").setValue(newReceipt)
    }


    private fun validateSaveAction(): Boolean {
        if (binding.receiptValue.text.toString().isEmpty()) {
            Toast.makeText(this, "Preencha o valor", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.receiptValue.text.toString().contains(",")) {
            Toast.makeText(this, "Utilize . para casas decimais", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.receiptDate.text.toString().isEmpty()) {
            Toast.makeText(this, "Preencha a data", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.receiptCategory.text.toString().isEmpty()) {
            Toast.makeText(this, "Preencha a categoria", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.receiptDescription.text.toString().isEmpty()) {
            Toast.makeText(this, "Preencha a descrição", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    companion object {
        fun getStaterIntent(context: Context) =
            Intent(context, ReceitasActivity::class.java)
    }
}