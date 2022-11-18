package com.jorgetargz.recycler.domain.usecases.personas

import com.jorgetargz.recycler.data.RepositorioPersonas
import javax.inject.Inject


class GetPersonasUseCase @Inject constructor(private val repositorioPersonas: RepositorioPersonas) {

    suspend fun invoke() = repositorioPersonas.getPersonas()
}