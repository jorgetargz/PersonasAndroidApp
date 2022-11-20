package com.jorgetargz.recycler.ui.edit_visita

import com.jorgetargz.recycler.domain.modelo.Visita

data class EditVisitaState(
    val mensaje: String?,
    val visitaMostrar: Visita,
    val visitaSinEditar: Visita?,
)