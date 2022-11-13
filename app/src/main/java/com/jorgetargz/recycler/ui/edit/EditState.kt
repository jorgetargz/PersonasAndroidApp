package com.jorgetargz.recycler.ui.edit

import com.jorgetargz.recycler.domain.modelo.Persona

data class EditState(
    val mensaje: String?,
    val persona: Persona,
    val onEdit: Boolean,
)