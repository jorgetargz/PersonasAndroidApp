package com.jorgetargz.recycler.domain.modelo

import java.time.LocalDate


private const val EMPTY_STRING = ""

data class Persona(
    val email: String = EMPTY_STRING,
    val nombre: String = EMPTY_STRING,
    var fnacimiento: LocalDate? = null,
    val telefono: String = EMPTY_STRING,
)

