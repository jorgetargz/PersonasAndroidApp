package com.jorgetargz.recycler.data.utils

import com.jorgetargz.recycler.data.modelo.PersonaEntity
import com.jorgetargz.recycler.domain.modelo.Persona

fun Persona.toPersonaEntity() : PersonaEntity {
    return PersonaEntity(this.email,this.nombre,this.fnacimiento!!,this.telefono)
}

fun PersonaEntity.toPersona() : Persona {
    return Persona(this.email,this.nombre, this.fnacimiento,this.telefono)
}