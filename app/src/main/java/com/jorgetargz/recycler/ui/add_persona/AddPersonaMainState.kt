package com.jorgetargz.recycler.ui.add_persona

import com.jorgetargz.recycler.domain.modelo.Persona

data class AddPersonaMainState(
    val mensaje: String? = null,
    val personaAdded: Persona? = null,
    val cleanFields: Boolean = false,
)