package com.manogarrafa.organizze.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.manogarrafa.organizze.R
import com.manogarrafa.organizze.commons.TypeMovimentacao
import com.manogarrafa.organizze.config.FirebaseConfig
import com.manogarrafa.organizze.databinding.ActivityPrincipalBinding
import com.manogarrafa.organizze.model.Movimentacao
import com.manogarrafa.organizze.model.User
import kotlin.properties.Delegates

class PrincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrincipalBinding
    private var expenseTotal by Delegates.notNull<Double>()
    private var receiptTotal by Delegates.notNull<Double>()
    private var resumeUser by Delegates.notNull<Double>()
    private var userName by Delegates.notNull<String>()
    private lateinit var valueEventListenerUser: ValueEventListener
    private lateinit var valueEventListenerMovimentacao: ValueEventListener
    private var movimentacoes: MutableList<Movimentacao?> = mutableListOf()
    private var calendarDate by Delegates.notNull<String>()
    private val adapterMovimentacao: MovimentacoesAdapter =
        MovimentacoesAdapter(this, movimentacoes as List<Movimentacao>)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            setSupportActionBar(contentPrimary.toolbar)
            contentPrimary.toolbar.showOverflowMenu()

            actionDespesa.setOnClickListener {
                DespesasActivity.getStaterIntent(this@PrincipalActivity).run(::startActivity)
            }
            actionReceita.setOnClickListener {
                ReceitasActivity.getStaterIntent(this@PrincipalActivity).run(::startActivity)
            }
        }

        configureCalendar()
        setupSwipe()
    }

    override fun onStart() {
        super.onStart()
        retrieveResume()
        retrieveMovimetacoes()
    }

    override fun onStop() {
        super.onStop()
        clearEventListener("usuarios", valueEventListenerUser)
        clearEventListener("movimentacao", valueEventListenerMovimentacao)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_exit -> {
                FirebaseConfig.getFirebaseAuth().signOut()
                MainActivity.getStaterIntent(this@PrincipalActivity).run(::startActivity)
                finish()
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configureCalendar() {
        val months: Array<CharSequence> = arrayOf(
            "Janeiro",
            "Fevereiro",
            "Março",
            "Abril",
            "Maio",
            "Junho",
            "Julho",
            "Agosto",
            "Setembro",
            "Outubro",
            "Novembro",
            "Dezembro"
        )
        with(binding.contentPrimary.calendarView) {
            calendarDate = "${String.format("%02d", currentDate.month + 1)}${currentDate.year}"
            setTitleMonths(months)
            setOnMonthChangedListener { _, date ->
                calendarDate = "${String.format("%02d", date.month + 1)}${date.year}"
                clearEventListener("movimentacao", valueEventListenerMovimentacao)
                retrieveMovimetacoes()
            }
        }
    }

    private fun retrieveResume() {
        valueEventListenerUser = FirebaseConfig.getUserDatabase("usuarios")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue<User>()
                    expenseTotal = user?.expensesTotal ?: 0.0
                    receiptTotal = user?.receiptTotal ?: 0.0
                    resumeUser = receiptTotal - expenseTotal
                    userName = user?.name ?: ""
                    setupResume()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun retrieveMovimetacoes() {
        valueEventListenerMovimentacao =
            FirebaseConfig.getUserDatabase("movimentacao").child(calendarDate)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        movimentacoes.clear()
                        for (data: DataSnapshot in snapshot.children) {
                            val movimentacao = data.getValue<Movimentacao>()
                            movimentacao?.key = data.key.toString()
                            movimentacoes.add(movimentacao)
                        }
                        with(binding.contentPrimary.recyclerMovimentacoes) {
                            layoutManager = LinearLayoutManager(this@PrincipalActivity)
                            adapter = adapterMovimentacao

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
    }

    private fun setupResume() = with(binding.contentPrimary) {
        textSaudacao.text = getString(R.string.saudação, userName)
        textSaldo.text = getString(R.string.price_template, resumeUser)
    }

    private fun clearEventListener(path: String, eventListener: ValueEventListener) {
        FirebaseConfig.getUserDatabase(path).removeEventListener(eventListener)
    }

    private fun setupSwipe() {
        val itemTouchHelper = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(
                    ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START or ItemTouchHelper.END
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                removeMovimentacao(viewHolder)
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.contentPrimary.recyclerMovimentacoes)
    }

    private fun removeMovimentacao(viewHolder: RecyclerView.ViewHolder) {
        AlertDialog.Builder(this)
            .setTitle("Excluir Movimentação").setMessage("Tem certeza?").setCancelable(false)
            .setPositiveButton(
                "Confirmar"
            ) { _, _ ->
                movimentacoes[viewHolder.adapterPosition]?.key?.let {
                    FirebaseConfig.getUserDatabase("movimentacao").child(calendarDate)
                        .child(it).removeValue()
                }
                movimentacoes[viewHolder.adapterPosition]?.let { updateTotal(it) }
                adapterMovimentacao.notifyItemRemoved(viewHolder.adapterPosition)
            }.setNegativeButton(
                "Cancelar"
            ) { _, _ ->
                Toast.makeText(
                    this@PrincipalActivity,
                    "Cancelado",
                    Toast.LENGTH_SHORT
                ).show()
                adapterMovimentacao.notifyItemChanged(viewHolder.adapterPosition)
            }.create().show()
    }

    private fun updateTotal(movimentacao: Movimentacao) {
        when (movimentacao.type) {
            TypeMovimentacao.Expense.toString() -> {
                expenseTotal -= movimentacao.value
                FirebaseConfig.getUserDatabase("usuarios").child("expensesTotal")
                    .setValue(expenseTotal)
            }
            TypeMovimentacao.Receipt.toString() -> {
                receiptTotal -= movimentacao.value
                FirebaseConfig.getUserDatabase("usuarios").child("receiptTotal")
                    .setValue(receiptTotal)
            }
        }
    }

    companion object {
        fun getStaterIntent(context: Context) = Intent(context, PrincipalActivity::class.java)
    }
}