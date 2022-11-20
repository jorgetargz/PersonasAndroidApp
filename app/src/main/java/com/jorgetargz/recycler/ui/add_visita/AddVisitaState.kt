package com.jorgetargz.recycler.ui.add_visita

import com.jorgetargz.recycler.domain.modelo.Visita

data class AddVisitaState(
    val mensaje: String? = null,
    val visitaAdded: Visita? = null,
    val cifList: List<String>? = null,
    val emailList: List<String>? = null,
    val cleanFields: Boolean = false,
)