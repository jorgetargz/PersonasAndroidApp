package com.jorgetargz.recycler.ui.listado_personas

import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.modelo.Tarjeta

data class ListPersonaState(
    val mensaje: String?,
    val lista: List<Persona>?,
    val personaDeleted: Persona?,
    val tarjetasDeleted: List<Tarjeta>?
)