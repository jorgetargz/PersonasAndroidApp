package com.jorgetargz.recycler.domain.usecases.hoteles

import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import java.util.regex.Pattern
import javax.inject.Inject

class ValidarHotelUseCase @Inject constructor() {

    operator fun invoke(
        cif: String,
        nombre: String,
        telefono: String,
        estrellas: String,
        stringProvider: StringProvider,
    ): String? {
        val regexEstrellas = Pattern.compile(Constantes.REGEX_ESTRELLAS)
        val regexCif = Pattern.compile(Constantes.REGEX_CIF)
        var error: String? = null
        if (nombre.isEmpty() || telefono.isEmpty() ||
            cif.isEmpty() || estrellas.isEmpty()
        ) {
            error = stringProvider.getString(R.string.rellena_campos)
        } else if (!regexCif.matcher(cif).matches()) {
            error = stringProvider.getString(R.string.cif_incorrecto)
        } else if (nombre.length < Constantes.MIN_NAME_LENGHT) {
            error = stringProvider.getString(R.string.nombre_incorrecto)
        } else if (!telefono.startsWith(Constantes.PLUS)) {
            error = stringProvider.getString(R.string.telefono_incorrecto)
        } else if (!regexEstrellas.matcher(estrellas).matches()) {
            error = stringProvider.getString(R.string.estrellas_incorrecta)
        }
        return error
    }
}