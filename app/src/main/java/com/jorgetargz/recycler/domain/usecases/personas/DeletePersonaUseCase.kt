package com.jorgetargz.recycler.domain.usecases.personas

import com.jorgetargz.recycler.data.RepositorioPersonas
import com.jorgetargz.recycler.data.RepositorioTarjetas
import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.modelo.Tarjeta
import javax.inject.Inject


class DeletePersonaUseCase @Inject constructor(
    private val repositorioPersonas: RepositorioPersonas,
    private val repositorioTarjetas: RepositorioTarjetas) {

    suspend fun invoke(persona: Persona) {
        val tarjetas: List<Tarjeta>  = repositorioPersonas.getTarjetasOfPersona(persona)
        repositorioTarjetas.deleteAllTarjetas(tarjetas)
        repositorioPersonas.deletePersona(persona)
    }
}