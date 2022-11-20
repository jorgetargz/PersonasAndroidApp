package com.jorgetargz.recycler.domain.usecases.visitas

import com.jorgetargz.recycler.data.RepositorioVisitas
import javax.inject.Inject

class GetVisitsUseCase @Inject constructor(private val repositorioVisitas: RepositorioVisitas) {

    suspend fun invoke() = repositorioVisitas.getVisitas()
}