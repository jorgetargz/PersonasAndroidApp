package com.jorgetargz.recycler.domain.modelo

import com.jorgetargz.recycler.domain.common.Constantes

data class Hotel(
    val cif: String = Constantes.EMPTY_STRING,
    val nombre: String = Constantes.EMPTY_STRING,
    val estrellas: Int = 0,
    val telefono: String = Constantes.EMPTY_STRING,
)