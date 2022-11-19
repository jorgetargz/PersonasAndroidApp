package com.jorgetargz.recycler.ui.listado_personas

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.databinding.ItemPersonaBinding
import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.ui.common.inflate

class PersonasAdapter(
    private val listPersonaActions: ListPersonaActions
) : ListAdapter<Persona, PersonasAdapter.ItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            parent.inflate(R.layout.item_persona),
            listPersonaActions
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) = with(holder) {
        val item = getItem(position)
        bind(item)
    }

    class ItemViewholder(
        itemView: View,
        private val listPersonaActions: ListPersonaActions
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemPersonaBinding.bind(itemView)

        fun bind(item: Persona) = with(binding) {
            tvNombre.text = item.nombre
            tvEmail.text = item.email

            buttonDelete.setOnClickListener {
                listPersonaActions.deletePersona(binding.tvEmail.text.toString())
            }

            buttonUpdate.setOnClickListener {
                listPersonaActions.editPersona(binding.tvEmail.text.toString())
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
