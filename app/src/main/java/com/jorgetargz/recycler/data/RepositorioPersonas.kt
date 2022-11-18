package com.jorgetargz.recycler.data

import com.jorgetargz.recycler.data.utils.toPersona
import com.jorgetargz.recycler.data.utils.toPersonaEntity
import com.jorgetargz.recycler.domain.modelo.Persona
import javax.inject.Inject

class RepositorioPersonas @Inject constructor(private val personasDao: PersonasDao) {

    suspend fun getPersonas() = personasDao.getAll().map { it.toPersona() }

    suspend fun getPersonaByEmail(first: String) = personasDao.findByEmail(first).toPersona()

    suspend fun insertPersona(persona: Persona) = personasDao.insert(persona.toPersonaEntity())

    suspend fun updatePersona(persona: Persona) = personasDao.update(persona.toPersonaEntity())

    suspend fun deletePersona(persona: Persona) = personasDao.delete(persona.toPersonaEntity())

}