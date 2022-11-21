package com.jorgetargz.recycler.ui.add_tarjeta

import com.jorgetargz.recycler.domain.modelo.Tarjeta

data class AddTarjetaMainState(
    val mensaje: String? = null,
    val tarjetaAdded: Tarjeta? = null,
    val cleanFields: Boolean = false,
)