package com.jorgetargz.recycler.ui.listado

import com.jorgetargz.recycler.domain.modelo.Persona

data class ListState(
    val mensaje: String?,
    val lista: List<Persona>?,
    val onDelete: Persona?,
)