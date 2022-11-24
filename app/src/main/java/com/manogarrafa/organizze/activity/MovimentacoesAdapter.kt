package com.manogarrafa.organizze.activity

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manogarrafa.organizze.R
import com.manogarrafa.organizze.commons.TypeMovimentacao
import com.manogarrafa.organizze.databinding.MovimentacaoItemBinding
import com.manogarrafa.organizze.model.Movimentacao

internal class MovimentacoesAdapter(
    private val context: Context,
    private val movimentacaos: List<Movimentacao>,
) : RecyclerView.Adapter<MovimentacoesAdapter.MovimentacaoListViewHolder>() {
    inner class MovimentacaoListViewHolder(private val binding: MovimentacaoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movimentacao) {
            binding.movimentacaoNome.text = item.description
            binding.movimentacaoCategoria.text = item.category
            binding.movimentacaoValor.text = context.getString(R.string.price_template, item.value)

            with(binding.movimentacaoValor) {
                when (item.type) {
                    TypeMovimentacao.Expense.toString() -> {
                        setTextColor(
                            context.getColor(R.color.colorAccentDespesa)
                        )
                        text = context.getString(R.string.negative_price_template, item.value)
                    }
                    TypeMovimentacao.Receipt.toString() -> {
                        setTextColor(
                            context.getColor(R.color.colorAccentReceita)
                        )
                        text = context.getString(R.string.price_template, item.value)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimentacaoListViewHolder =
        MovimentacaoListViewHolder(
            binding = MovimentacaoItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: MovimentacaoListViewHolder, position: Int) {
        holder.bind(movimentacaos[position])
    }

    override fun getItemCount(): Int = movimentacaos.size

}