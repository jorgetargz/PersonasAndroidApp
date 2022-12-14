package com.jorgetargz.recycler.ui.add_hotel

import com.jorgetargz.recycler.domain.modelo.Hotel

data class AddHotelState(
    val mensaje: String? = null,
    val hotelAdded: Hotel? = null,
    val cleanFields: Boolean = false,
)