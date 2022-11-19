package com.jorgetargz.recycler.domain.usecases.hoteles

import com.jorgetargz.recycler.data.RepositorioHoteles
import javax.inject.Inject

class GetHotelByCIF @Inject constructor(private val repositorioHoteles: RepositorioHoteles) {

    suspend fun invoke(cif: String) = repositorioHoteles.getHotelByCIF(cif)
}