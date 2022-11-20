package com.jorgetargz.recycler.domain.usecases.visitas

import com.jorgetargz.recycler.data.RepositorioVisitas
import javax.inject.Inject

class GetVisitsByHotelCIFUseCase @Inject constructor(private val repositorioVisitas: RepositorioVisitas) {

    suspend fun invoke(cif: String) = repositorioVisitas.getVisitasByCIF(cif)
}