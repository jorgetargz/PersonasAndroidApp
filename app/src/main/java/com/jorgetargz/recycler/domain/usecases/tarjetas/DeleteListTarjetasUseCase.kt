package com.jorgetargz.recycler.domain.usecases.tarjetas

import com.jorgetargz.recycler.data.RepositorioTarjetas
import com.jorgetargz.recycler.domain.modelo.Tarjeta
import javax.inject.Inject


class DeleteListTarjetasUseCase @Inject constructor(private val repositorioTarjetas: RepositorioTarjetas) {

    suspend fun invoke(tarjetas: List<Tarjeta>) = repositorioTarjetas.deleteAllTarjetas(tarjetas)
}