package com.jorgetargz.recycler.data

import com.jorgetargz.recycler.data.room.TarjetasDao
import com.jorgetargz.recycler.data.room.utils.toTarjeta
import com.jorgetargz.recycler.data.room.utils.toTarjetaEntity
import com.jorgetargz.recycler.domain.modelo.Tarjeta
import javax.inject.Inject

class RepositorioTarjetas @Inject constructor(
    private val tarjetasDao: TarjetasDao
) {
    suspend fun insertTarjeta(tarjeta: Tarjeta) = tarjetasDao.insert(tarjeta.toTarjetaEntity())

    suspend fun deleteTarjeta(tarjeta: Tarjeta) = tarjetasDao.delete(tarjeta.toTarjetaEntity())

    suspend fun deleteAllTarjetas(tarjetas: List<Tarjeta>) =
        tarjetasDao.deleteAll(tarjetas.map { it.toTarjetaEntity() })

    suspend fun getTarjetaByNumero(numero: String): Tarjeta =
        tarjetasDao.getByNumero(numero).toTarjeta()
}
