package com.jorgetargz.recycler.data

import com.jorgetargz.recycler.data.room.PersonaHotelDao
import com.jorgetargz.recycler.data.room.utils.toPersonaHotelCrossRef
import com.jorgetargz.recycler.data.room.utils.toVisita
import com.jorgetargz.recycler.domain.modelo.Visita
import javax.inject.Inject

class RepositorioVisitas @Inject constructor(
    private val personaHotelDao: PersonaHotelDao,
) {

    suspend fun getVisitas() = personaHotelDao.getAll().map { it.toVisita() }

    suspend fun getVisitasByEmail(email: String) =
        personaHotelDao.findByEmail(email).map { it.toVisita() }

    suspend fun getVisitasByCIF(cif: String) =
        personaHotelDao.findByCif(cif).map { it.toVisita() }

    suspend fun getVisitaByEmailAndCIF(email: String, cif: String) =
        personaHotelDao.findByEmailAndCif(email, cif)?.toVisita()

    suspend fun addVisita(visita: Visita) =
        personaHotelDao.insert(visita.toPersonaHotelCrossRef())

    suspend fun updateVisita(visita: Visita) =
        personaHotelDao.update(visita.toPersonaHotelCrossRef())

    suspend fun removeVisita(visita: Visita) =
        personaHotelDao.delete(visita.toPersonaHotelCrossRef())

}