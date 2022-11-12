package com.jorgetargz.recycler.domain.usecases.personas

import com.jorgetargz.recycler.data.RepositorioPersonas


class GetPersonasUseCase(val repositorioPersonas: RepositorioPersonas) {

    suspend fun invoke() = repositorioPersonas.getPersonas()
}