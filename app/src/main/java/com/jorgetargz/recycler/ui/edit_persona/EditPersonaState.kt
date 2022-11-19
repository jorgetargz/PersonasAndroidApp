package com.jorgetargz.recycler.ui.edit_persona

import com.jorgetargz.recycler.domain.modelo.Persona

data class EditPersonaState(
    val mensaje: String?,
    val personaMostrar: Persona,
    val personaSinEditar: Persona?,
)