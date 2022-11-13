package com.jorgetargz.recycler.ui.main

import com.jorgetargz.recycler.domain.modelo.Persona

data class MainState(
    val mensaje: String? = null,
    val personaAdded: Persona? = null,
    val cleanFields: Boolean = false,
)