package com.jorgetargz.recycler.ui.listado_visitas

import com.jorgetargz.recycler.domain.modelo.Visita

data class ListVisitasState(
    val mensaje: String?,
    val lista: List<Visita>?,
    val visitaDeleted: Visita?,
)