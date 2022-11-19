package com.jorgetargz.recycler.domain.usecases.personas

import com.jorgetargz.recycler.data.RepositorioPersonas
import com.jorgetargz.recycler.domain.modelo.Hotel
import com.jorgetargz.recycler.domain.modelo.Persona
import javax.inject.Inject

class UnRegisterHotelVisitUseCase @Inject constructor(private val repositorioPersonas: RepositorioPersonas) {

    suspend fun invoke(persona: Persona, hotel: Hotel) =
        repositorioPersonas.removePersonaFromHotel(persona, hotel)
}