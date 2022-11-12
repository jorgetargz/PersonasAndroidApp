package com.jorgetargz.recycler.domain.usecases.personas

import com.jorgetargz.recycler.data.RepositorioPersonas
import com.jorgetargz.recycler.domain.modelo.Persona


class UpdatePersonaUseCase(val repositorioPersonas: RepositorioPersonas) {

    suspend fun invoke(persona: Persona) = repositorioPersonas.updatePersona(persona)
}