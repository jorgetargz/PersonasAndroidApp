package com.jorgetargz.recycler.domain.usecases.tarjetas

import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import java.time.LocalDate
import java.util.regex.Pattern
import javax.inject.Inject

class ValidarTarjetaUseCase @Inject constructor() {

    operator fun invoke(
        numero: String,
        fechaCaducidad: String,
        cvv: String,
        nombreTitular: String,
        stringProvider: StringProvider,
    ): String? {
        var error: String? = null
        val datePattern: Pattern = Pattern.compile(Constantes.REGEX_DATE)
        val cvvPattern: Pattern = Pattern.compile(Constantes.REGEX_CVV)
        if (numero.isEmpty() || fechaCaducidad.isEmpty() || cvv.isEmpty() || nombreTitular.isEmpty()) {
            error = stringProvider.getString(R.string.rellena_campos)
        }
        if (numero.length != 16) {
            error = stringProvider.getString(R.string.error_numero_tarjeta)
        } else if (!datePattern.matcher(fechaCaducidad).matches()) {
            error = stringProvider.getString(R.string.error_fecha_caducidad_tarjeta)
        } else if (LocalDate.parse(fechaCaducidad).isBefore(LocalDate.now())) {
            error = stringProvider.getString(R.string.error_fecha_caducidad_pasada)
        } else if (!cvvPattern.matcher(cvv).matches()) {
            error = stringProvider.getString(R.string.error_cvv_tarjeta)
        } else if (nombreTitular.length < 2) {
            error = stringProvider.getString(R.string.error_nombre_titular_tarjeta)
        }
        return error
    }
}