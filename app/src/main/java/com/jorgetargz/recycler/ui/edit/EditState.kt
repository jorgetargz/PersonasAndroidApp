package com.jorgetargz.recycler.ui.edit

import com.jorgetargz.recycler.domain.modelo.Persona

data class EditState(
    val mensaje: String?,
    val personaMostrar: Persona,
    val personaSinEditar: Persona?,
)