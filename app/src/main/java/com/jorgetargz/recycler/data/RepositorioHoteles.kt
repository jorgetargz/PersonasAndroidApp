package com.jorgetargz.recycler.data

import com.jorgetargz.recycler.data.room.HotelesDao
import com.jorgetargz.recycler.data.room.PersonaHotelDao
import com.jorgetargz.recycler.data.room.utils.toHotel
import com.jorgetargz.recycler.data.room.utils.toHotelEntity
import com.jorgetargz.recycler.data.room.utils.toPersona
import com.jorgetargz.recycler.domain.modelo.Hotel
import javax.inject.Inject

class RepositorioHoteles @Inject constructor(
    private val hotelesDao: HotelesDao,
    private val personaHotelDao: PersonaHotelDao,
) {

    suspend fun getHoteles() = hotelesDao.getAll().map { it.toHotel() }

    suspend fun getHotelByCIF(cif: String) = hotelesDao.findByCIF(cif).toHotel()

    suspend fun insertHotel(hotel: Hotel) = hotelesDao.insert(hotel.toHotelEntity())

    suspend fun updateHotel(hotel: Hotel) = hotelesDao.update(hotel.toHotelEntity())

    suspend fun deleteHotel(hotel: Hotel) {
        val visitasByHotelCIF = personaHotelDao.findByCif(hotel.cif)
        hotelesDao.deleteWithVisits(hotel.toHotelEntity(), visitasByHotelCIF)
    }

    suspend fun getPersonasOfHotel(hotel: Hotel) =
        hotelesDao.getPersonasOfHotel(hotel.cif).flatMap { it.personas.map { it.toPersona() } }


}