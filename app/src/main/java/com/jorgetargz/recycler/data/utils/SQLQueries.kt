package com.jorgetargz.recycler.data.utils

object SQLQueries {

    const val SELECT_ALL_PERSONAS = "SELECT * FROM personas"
    const val SELECT_PERSONA_BY_EMAIL = "SELECT * FROM personas WHERE email LIKE :first LIMIT 1"
}