package com.jorgetargz.recycler.data.room.utils

object SQLQueries {

    const val SELECT_ALL_PERSONAS = "SELECT * FROM personas"
    const val SELECT_PERSONA_BY_EMAIL = "SELECT * FROM personas WHERE email LIKE :email LIMIT 1"
    const val SELET_ALL_HOTELS = "SELECT * FROM hoteles"
    const val SELECT_HOTEL_BY_CIF = "SELECT * FROM hoteles WHERE CIF LIKE :cif LIMIT 1"
}