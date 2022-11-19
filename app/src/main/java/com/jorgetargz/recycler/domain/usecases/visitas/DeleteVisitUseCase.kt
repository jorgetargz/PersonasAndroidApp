package com.jorgetargz.recycler.domain.usecases.visitas

import com.jorgetargz.recycler.data.RepositorioVisitas
import com.jorgetargz.recycler.domain.modelo.Visita
import javax.inject.Inject

class DeleteVisitUseCase @Inject constructor(private val repositorioVisitas: RepositorioVisitas) {

    suspend fun invoke(visita: Visita) =
        repositorioVisitas.removeVisita(visita)
}