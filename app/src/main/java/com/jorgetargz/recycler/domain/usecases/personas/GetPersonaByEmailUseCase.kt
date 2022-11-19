package com.jorgetargz.recycler.domain.usecases.personas

import com.jorgetargz.recycler.data.RepositorioPersonas
import javax.inject.Inject


class GetPersonaByEmailUseCase @Inject constructor(private val repositorioPersonas: RepositorioPersonas) {

    suspend fun invoke(email: String) = repositorioPersonas.getPersonaByEmail(email)
}