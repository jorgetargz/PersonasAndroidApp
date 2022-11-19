package com.jorgetargz.recycler.ui.listado_hoteles

import com.jorgetargz.recycler.domain.modelo.Hotel

data class ListHotelesState(
    val mensaje: String?,
    val lista: List<Hotel>?,
    val hotelDeleted: Hotel?,
)