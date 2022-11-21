package com.jorgetargz.recycler.data.room.utils

object SQLQueries {

    const val SELECT_ALL_PERSONAS = "SELECT * FROM personas"
    const val SELECT_PERSONA_BY_EMAIL = "SELECT * FROM personas WHERE email LIKE :email LIMIT 1"
    const val SELECT_TARJETA_BY_NUMERO = "SELECT * FROM tarjetas WHERE numeroTarjeta = :numero LIMIT 1"
    const val SELET_ALL_HOTELS = "SELECT * FROM hoteles"
    const val SELECT_HOTEL_BY_CIF = "SELECT * FROM hoteles WHERE cif LIKE :cif LIMIT 1"
    const val SELECT_ALL_VISITAS = "SELECT * FROM personas_hoteles"
    const val SELECT_VISITAS_BY_EMAIL = "SELECT * FROM personas_hoteles WHERE email LIKE :email"
    const val SELECT_VISITAS_BY_CIF = "SELECT * FROM personas_hoteles WHERE cif LIKE :cif"
    const val SELECT_VISITAS_BY_EMAIL_AND_CIF = "SELECT * FROM personas_hoteles WHERE email LIKE :email AND cif LIKE :cif LIMIT 1"
}