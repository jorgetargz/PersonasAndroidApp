package com.jorgetargz.recycler.ui.edit_hotel

import com.jorgetargz.recycler.domain.modelo.Hotel

data class EditHotelState(
    val mensaje: String?,
    val hotelMostrar: Hotel,
    val hotelSinEditar: Hotel?,
)