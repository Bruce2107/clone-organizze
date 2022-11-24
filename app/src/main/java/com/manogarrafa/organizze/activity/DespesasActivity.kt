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
import com.manogarrafa.organizze.databinding.ActivityDespesasBinding
import com.manogarrafa.organizze.model.Movimentacao
import com.manogarrafa.organizze.model.User
import com.manogarrafa.organizze.utils.CustomDate
import kotlin.properties.Delegates

class DespesasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDespesasBinding
    private var expenseTotal by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDespesasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        retrieveTotal()
    }

    private fun setupView() = with(binding) {
        expenseDate.setText(CustomDate.getCurrentDate())
        floatingActionButton.setOnClickListener {
            saveExpense()
        }
    }

    private fun saveExpense() {
        with(binding) {
            if (validateSaveAction()) {
                val value = expenseValue.text.toString().toDouble()
                val description = expenseDescription.text.toString()
                val date = expenseDate.text.toString()
                val category = expenseCategory.text.toString()
                Movimentacao(
                    value = value,
                    description = description,
                    date = date,
                    category = category,
                    type = TypeMovimentacao.Expense.toString()
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
                    expenseTotal = user?.expensesTotal ?: 0.0
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value

                }
            })
    }

    private fun updateTotal() {
        val value = binding.expenseValue.text.toString().toDouble()
        val newExpense = value + expenseTotal
        FirebaseConfig.getUserDatabase("usuarios")
            .child("expensesTotal").setValue(newExpense)
    }


    private fun validateSaveAction(): Boolean {
        if (binding.expenseValue.text.toString().isEmpty()) {
            Toast.makeText(this, "Preencha o valor", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.expenseValue.text.toString().contains(",")) {
            Toast.makeText(this, "Utilize . para casas decimais", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.expenseDate.text.toString().isEmpty()) {
            Toast.makeText(this, "Preencha a data", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.expenseCategory.text.toString().isEmpty()) {
            Toast.makeText(this, "Preencha a categoria", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.expenseDescription.text.toString().isEmpty()) {
            Toast.makeText(this, "Preencha a descrição", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    companion object {
        fun getStaterIntent(context: Context) =
            Intent(context, DespesasActivity::class.java)
    }
}