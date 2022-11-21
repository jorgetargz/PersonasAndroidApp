package com.jorgetargz.recycler.ui.listado_tarjetas

import com.jorgetargz.recycler.domain.modelo.Tarjeta

data class ListTarjetasState(
    val mensaje: String?,
    val lista: List<Tarjeta>?,
    val tarjetaDeleted: Tarjeta?,
)