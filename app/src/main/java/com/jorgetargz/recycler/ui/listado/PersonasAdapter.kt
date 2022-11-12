package com.jorgetargz.recycler.ui.listado

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.databinding.ItemPersonaBinding
import com.jorgetargz.recycler.domain.modelo.Persona

class PersonasAdapter(
    private val listActions: ListActions
) : ListAdapter<Persona, PersonasAdapter.ItemViewholder>(DiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_persona, parent, false),
            listActions
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) = with(holder) {
        val item = getItem(position)
        bind(item)
    }

    class ItemViewholder(
        itemView: View,
        private val listActions: ListActions)
        : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemPersonaBinding.bind(itemView)

        fun bind(item: Persona) = with(binding) {
            tvNombre.text = item.nombre
            tvEmail.text = item.email

            buttonDelete.setOnClickListener {
                listActions.deletePersona(binding.tvEmail.text.toString())
            }

            buttonUpdate.setOnClickListener {
                listActions.editPersona(binding.tvEmail.text.toString())
            }
        }


    }
    class DiffCallback : DiffUtil.ItemCallback<Persona>() {
        override fun areItemsTheSame(oldItem: Persona, newItem: Persona): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: Persona, newItem: Persona): Boolean {
            return oldItem == newItem
        }
    }
}
