package com.jorgetargz.recycler.domain.usecases.visitas

import com.jorgetargz.recycler.data.RepositorioVisitas
import javax.inject.Inject

class GetVisitsByPersonEmailUseCase @Inject constructor(private val repositorioVisitas: RepositorioVisitas) {

    suspend fun invoke(email: String) = repositorioVisitas.getVisitasByEmail(email)
}