package com.jorgetargz.recycler.domain.usecases.personas

import com.jorgetargz.recycler.data.RepositorioPersonas
import javax.inject.Inject


class GetPersonaUseCase @Inject constructor(private val repositorioPersonas: RepositorioPersonas) {

    suspend fun invoke(email: String) = repositorioPersonas.getPersonaByEmail(email)
}