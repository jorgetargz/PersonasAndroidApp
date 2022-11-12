package com.jorgetargz.recycler.domain.usecases.personas

import com.jorgetargz.recycler.data.RepositorioPersonas


class GetPersonaUseCase(val repositorioPersonas: RepositorioPersonas) {

    suspend fun invoke(email: String) = repositorioPersonas.getPersonaByEmail(email)
}