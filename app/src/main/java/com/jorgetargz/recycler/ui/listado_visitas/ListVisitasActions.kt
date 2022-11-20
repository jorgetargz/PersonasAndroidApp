package com.jorgetargz.recycler.ui.listado_visitas

interface ListVisitasActions {
    fun editVisita(cif: String, email: String)
    fun deleteVisita(cif: String, email: String)
}