package com.jorgetargz.recycler.ui.listado_personas

import com.jorgetargz.recycler.domain.modelo.Persona

data class ListPersonaState(
    val mensaje: String?,
    val lista: List<Persona>?,
    val personaDeleted: Persona?,
)