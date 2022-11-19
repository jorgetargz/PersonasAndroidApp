package com.jorgetargz.recycler.data

import com.jorgetargz.recycler.data.room.PersonaHotelDao
import com.jorgetargz.recycler.data.room.PersonasDao
import com.jorgetargz.recycler.data.room.modelo.PersonaHotelCrossRef
import com.jorgetargz.recycler.data.room.utils.toHotel
import com.jorgetargz.recycler.data.room.utils.toPersona
import com.jorgetargz.recycler.data.room.utils.toPersonaEntity
import com.jorgetargz.recycler.domain.modelo.Hotel
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

    suspend fun deletePersona(persona: Persona) = personasDao.delete(persona.toPersonaEntity())

    suspend fun getHotelesOfPersona(persona: Persona) =
        personasDao.getHotelsOfPersona(persona.email).map { it.hotels.map { it.toHotel() } }

    suspend fun addPersonaToHotel(persona: Persona, hotel: Hotel) =
        personaHotelDao.insert(PersonaHotelCrossRef(persona.email, hotel.cif))

    suspend fun removePersonaFromHotel(persona: Persona, hotel: Hotel) =
        personaHotelDao.delete(PersonaHotelCrossRef(persona.email, hotel.cif))

}