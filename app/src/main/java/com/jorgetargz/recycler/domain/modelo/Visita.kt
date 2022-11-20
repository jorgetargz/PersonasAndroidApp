package com.jorgetargz.recycler.domain.modelo

import com.jorgetargz.recycler.domain.common.Constantes

data class Visita(
    val emailPersona: String = Constantes.EMPTY_STRING,
    val cifHotel: String = Constantes.EMPTY_STRING,
    val valoracion: String = Constantes.EMPTY_STRING,
)