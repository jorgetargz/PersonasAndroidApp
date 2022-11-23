package com.jorgetargz.recycler.data

import com.jorgetargz.recycler.data.room.PersonaHotelDao
import com.jorgetargz.recycler.data.room.PersonasDao
import com.jorgetargz.recycler.data.room.utils.toHotel
import com.jorgetargz.recycler.data.room.utils.toPersona
import com.jorgetargz.recycler.data.room.utils.toPersonaEntity
import com.jorgetargz.recycler.data.room.utils.toTarjeta
import com.jorgetargz.recycler.domain.modelo.Persona
import javax.inject.Inject

class RepositorioPersonas @Inject constructor(
    private val personasDao: PersonasDao,
    private val personaHotelDao: PersonaHotelDao,
) {

    suspend fun getPersonas() = personasDao.getAll().map { it.toPersona() }

    suspend fun getPersonaByEmail(email: String) = personasDao.findByEmail(email).toPersona()

    suspend fun insertPersona(persona: Persona) = personasDao.insert(persona.toPersonaEntity())

    suspend fun updatePersona(persona: Persona) = personasDao.update(persona.toPersonaEntity())

    suspend fun deletePersona(persona: Persona) {
        val visitas = personaHotelDao.findByEmail(persona.email)
        val tarjetas = personasDao.getTarjetasOfPersona(persona.email).flatMap { it.tarjetas }
        personasDao.deleteWithVisitsAndCards(persona.toPersonaEntity(), visitas, tarjetas)
    }

    suspend fun getHotelesOfPersona(persona: Persona) =
        personasDao.getHotelsOfPersona(persona.email).flatMap { it.hotels }
            .map { it.toHotel() }

    suspend fun getTarjetasOfPersona(persona: Persona) =
        personasDao.getTarjetasOfPersona(persona.email).flatMap { it.tarjetas }
            .map { it.toTarjeta() }
}