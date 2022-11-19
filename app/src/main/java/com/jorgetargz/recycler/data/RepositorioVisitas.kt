package com.jorgetargz.recycler.data

import com.jorgetargz.recycler.data.room.PersonaHotelDao
import com.jorgetargz.recycler.data.room.modelo.PersonaHotelCrossRef
import com.jorgetargz.recycler.domain.modelo.Visita
import javax.inject.Inject

class RepositorioVisitas @Inject constructor(
    private val personaHotelDao: PersonaHotelDao,
) {
    suspend fun addVisita(visita: Visita) =
        personaHotelDao.insert(
            PersonaHotelCrossRef(
                visita.persona.email,
                visita.hotel.cif,
                visita.valoracion
            )
        )

    suspend fun updateVisita(visita: Visita) =
        personaHotelDao.update(
            PersonaHotelCrossRef(
                visita.persona.email,
                visita.hotel.cif,
                visita.valoracion
            )
        )

    suspend fun removeVisita(visita: Visita) =
        personaHotelDao.delete(
            PersonaHotelCrossRef(
                visita.persona.email,
                visita.hotel.cif,
                visita.valoracion
            )
        )

}