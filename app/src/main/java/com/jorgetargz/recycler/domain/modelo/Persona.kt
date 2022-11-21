package com.jorgetargz.recycler.domain.modelo

import com.jorgetargz.recycler.domain.common.Constantes
import java.time.LocalDate

data class Persona(
    val email: String = Constantes.EMPTY_STRING,
    val nombre: String = Constantes.EMPTY_STRING,
    val fnacimiento: LocalDate? = null,
    val telefono: String = Constantes.EMPTY_STRING,
)

