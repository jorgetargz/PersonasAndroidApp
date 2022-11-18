package com.jorgetargz.recycler.domain.usecases.personas

import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import java.util.regex.Pattern
import javax.inject.Inject

class ValidarPersonaUseCase @Inject constructor() {

    operator fun invoke(
        email: String,
        nombre: String,
        telefono: String,
        fnacimiento: String,
        stringProvider: StringProvider,
    ): String? {
        var error: String? = null
        val datePattern: Pattern = Pattern.compile(Constantes.REGEX_DATE)
        if (nombre.isEmpty() || telefono.isEmpty() ||
            email.isEmpty() || fnacimiento.isEmpty()
        ) {
            error = stringProvider.getString(R.string.rellena_campos)
        } else if (!email.contains(Constantes.AT) ||
            !email.contains(Constantes.DOT)
        ) {
            error = stringProvider.getString(R.string.email_incorrecto)
        } else if (nombre.length < Constantes.MIN_NAME_LENGHT) {
            error = stringProvider.getString(R.string.nombre_incorrecto)
        } else if (!telefono.startsWith(Constantes.PLUS)) {
            error = stringProvider.getString(R.string.telefono_incorrecto)
        } else if (!datePattern.matcher(fnacimiento).matches()) {
            error = stringProvider.getString(R.string.fecha_incorrecta)
        }
        return error
    }
}