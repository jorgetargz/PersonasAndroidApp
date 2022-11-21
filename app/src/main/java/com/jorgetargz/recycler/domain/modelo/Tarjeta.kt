package com.jorgetargz.recycler.domain.modelo

import com.jorgetargz.recycler.domain.common.Constantes
import java.time.LocalDate

data class Tarjeta(
    val numero: String = Constantes.EMPTY_STRING,
    val fechaCaducidad: LocalDate? = null,
    val cvv: Int = 0,
    val emailPersona: String = Constantes.EMPTY_STRING,
    val nombreTitular: String = Constantes.EMPTY_STRING,
)