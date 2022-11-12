package com.jorgetargz.recycler.data

import com.jorgetargz.PersonaEntity
import com.jorgetargz.recycler.domain.modelo.Persona

fun Persona.toPersonaEntity() : PersonaEntity {
    return PersonaEntity(this.email,this.nombre,this.fnacimiento!!,this.telefono)
}

fun PersonaEntity.toPersona() : Persona {
    return Persona(this.email,this.nombre, this.fnacimiento,this.telefono)
}