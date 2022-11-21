package com.jorgetargz.recycler.domain.usecases.tarjetas

import com.jorgetargz.recycler.data.RepositorioTarjetas
import com.jorgetargz.recycler.domain.modelo.Tarjeta
import javax.inject.Inject


class AddTarjetaUseCase @Inject constructor(private val repositorioTarjetas: RepositorioTarjetas) {

    suspend fun invoke(tarjeta: Tarjeta) = repositorioTarjetas.insertTarjeta(tarjeta)
}