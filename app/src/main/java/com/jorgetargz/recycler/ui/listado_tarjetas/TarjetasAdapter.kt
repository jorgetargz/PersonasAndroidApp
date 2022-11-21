package com.jorgetargz.recycler.ui.listado_tarjetas

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.databinding.ItemTarjetaBinding
import com.jorgetargz.recycler.domain.modelo.Tarjeta
import com.jorgetargz.recycler.ui.common.inflate

class TarjetasAdapter(
    private val listTarjetasActions: ListTarjetasActions
) : ListAdapter<Tarjeta, TarjetasAdapter.ItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            parent.inflate(R.layout.item_tarjeta),
            listTarjetasActions
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) = with(holder) {
        val item = getItem(position)
        bind(item)
    }

    class ItemViewholder(
        itemView: View,
        private val listTarjetasActions1: ListTarjetasActions
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemTarjetaBinding.bind(itemView)

        fun bind(item: Tarjeta) = with(binding) {
            tvNombre.text = item.nombreTitular
            tvNumero.text = item.numero.replaceRange(4, 12, "********")

            buttonDelete.setOnClickListener {
                listTarjetasActions1.deleteTarjeta(binding.tvNumero.text.toString())
            }

            buttonView.setOnClickListener {
                listTarjetasActions1.viewTarjeta(binding.tvNumero.text.toString())
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Tarjeta>() {
        override fun areItemsTheSame(oldItem: Tarjeta, newItem: Tarjeta): Boolean {
            return oldItem.numero == newItem.numero
        }

        override fun areContentsTheSame(oldItem: Tarjeta, newItem: Tarjeta): Boolean {
            return oldItem == newItem
        }
    }
}
