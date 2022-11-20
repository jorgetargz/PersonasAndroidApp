package com.jorgetargz.recycler.domain.usecases.visitas

import com.jorgetargz.recycler.data.RepositorioVisitas
import javax.inject.Inject

class GetVisitByCIFAndEmailUseCase @Inject constructor(private val repositorioVisitas: RepositorioVisitas) {

    suspend fun invoke(cif: String, email: String) =
        repositorioVisitas.getVisitaByEmailAndCIF(cif, email)
}