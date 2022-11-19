package com.jorgetargz.recycler.domain.usecases.hoteles

import com.jorgetargz.recycler.data.RepositorioHoteles
import com.jorgetargz.recycler.domain.modelo.Hotel
import javax.inject.Inject

class AddHotelUseCase @Inject constructor(private val repositorioHoteles: RepositorioHoteles) {

    suspend fun invoke(hotel: Hotel) = repositorioHoteles.insertHotel(hotel)
}