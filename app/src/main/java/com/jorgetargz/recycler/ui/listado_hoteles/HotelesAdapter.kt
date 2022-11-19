package com.jorgetargz.recycler.ui.listado_hoteles

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.databinding.ItemHotelBinding
import com.jorgetargz.recycler.domain.modelo.Hotel
import com.jorgetargz.recycler.ui.common.inflate

class HotelesAdapter(
    private val listHotelesActions: ListHotelesActions
) : ListAdapter<Hotel, HotelesAdapter.ItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            parent.inflate(R.layout.item_hotel),
            listHotelesActions
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) = with(holder) {
        val item = getItem(position)
        bind(item)
    }

    class ItemViewholder(
        itemView: View,
        private val listHotelesActions: ListHotelesActions
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemHotelBinding.bind(itemView)

        fun bind(item: Hotel) = with(binding) {
            tvNombre.text = item.nombre
            tvCif.text = item.cif

            buttonDelete.setOnClickListener {
                listHotelesActions.deleteHotel(binding.tvCif.text.toString())
            }

            buttonView.setOnClickListener {
                listHotelesActions.editHotel(binding.tvCif.text.toString())
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Hotel>() {
        override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem.cif == newItem.cif
        }

        override fun areContentsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem == newItem
        }
    }
}
