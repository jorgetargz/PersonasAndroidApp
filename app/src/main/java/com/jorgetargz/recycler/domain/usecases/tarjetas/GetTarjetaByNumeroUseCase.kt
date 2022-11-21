package com.jorgetargz.recycler.domain.usecases.tarjetas

import com.jorgetargz.recycler.data.RepositorioTarjetas
import javax.inject.Inject

class GetTarjetaByNumeroUseCase @Inject constructor(private val repositorioTarjetas: RepositorioTarjetas) {

    suspend fun invoke(numero: String) = repositorioTarjetas.getTarjetaByNumero(numero)
}