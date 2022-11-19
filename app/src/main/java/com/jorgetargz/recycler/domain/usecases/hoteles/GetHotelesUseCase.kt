package com.jorgetargz.recycler.domain.usecases.hoteles

import com.jorgetargz.recycler.data.RepositorioHoteles
import javax.inject.Inject

class GetHotelesUseCase @Inject constructor(private val repositorioHoteles: RepositorioHoteles) {

    suspend fun invoke() = repositorioHoteles.getHoteles()
}