package com.jorgetargz.recycler.ui.listado_visitas

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.databinding.ItemVisitaBinding
import com.jorgetargz.recycler.domain.modelo.Visita
import com.jorgetargz.recycler.ui.common.inflate

class VisitasAdapter(
    private val listVisitasActions: ListVisitasActions
) : ListAdapter<Visita, VisitasAdapter.ItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            parent.inflate(R.layout.item_visita),
            listVisitasActions
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) = with(holder) {
        val item = getItem(position)
        bind(item)
    }

    class ItemViewholder(
        itemView: View,
        private val listVisitasActions: ListVisitasActions
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemVisitaBinding.bind(itemView)

        fun bind(item: Visita) = with(binding) {
            tvEmail.text = item.emailPersona
            tvCif.text = item.cifHotel

            buttonDelete.setOnClickListener {
                listVisitasActions.deleteVisita(tvCif.text.toString(), tvEmail.text.toString())
            }

            buttonView.setOnClickListener {
                listVisitasActions.editVisita(tvCif.text.toString(), tvEmail.text.toString())
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Visita>() {
        override fun areItemsTheSame(oldItem: Visita, newItem: Visita): Boolean {
            return oldItem.cifHotel == newItem.cifHotel && oldItem.emailPersona == newItem.emailPersona
        }

        override fun areContentsTheSame(oldItem: Visita, newItem: Visita): Boolean {
            return oldItem == newItem
        }
    }
}
