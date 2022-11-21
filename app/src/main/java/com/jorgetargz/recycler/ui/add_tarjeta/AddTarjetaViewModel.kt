package com.jorgetargz.recycler.ui.add_tarjeta

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Tarjeta
import com.jorgetargz.recycler.domain.usecases.tarjetas.AddTarjetaUseCase
import com.jorgetargz.recycler.domain.usecases.tarjetas.DeleteTarjetaUseCase
import com.jorgetargz.recycler.domain.usecases.tarjetas.ValidarTarjetaUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AddTarjetaViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val validarTarjetaUseCase: ValidarTarjetaUseCase,
    private val addTarjetaUseCase: AddTarjetaUseCase,
    private val deleteTarjetaUseCase: DeleteTarjetaUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        AddTarjetaMainState(null)
    )

    val uiState: LiveData<AddTarjetaMainState> get() = _uiState

    private fun addTarjeta(
        email: String,
        titular: String,
        numero: String,
        caducidad: String,
        cvv: String
    ) {
        val errorValidacion =
            validarTarjetaUseCase(numero, caducidad, cvv, titular, stringProvider)
        if (errorValidacion == null) {
            val tarjeta = Tarjeta(
                numero = numero,
                fechaCaducidad = LocalDate.parse(
                    caducidad, DateTimeFormatter.ofPattern(Constantes.DATE_FORMAT)
                ),
                cvv = cvv.toInt(),
                emailPersona = email,
                nombreTitular = titular
            )
            viewModelScope.launch {
                try {
                    addTarjetaUseCase.invoke(tarjeta)
                    _uiState.value = _uiState.value?.copy(
                        mensaje = stringProvider.getString(R.string.tarjeta_añadida),
                        tarjetaAdded = tarjeta,
                        cleanFields = true,
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value?.copy(
                        mensaje = stringProvider.getString(R.string.error_añadir_tarjeta),
                        tarjetaAdded = null,
                        cleanFields = false,
                    )
                }
            }
        } else {
            _uiState.value = _uiState.value?.copy(
                mensaje = errorValidacion,
            )
        }
    }

    private fun undoAddTarjeta(tarjeta: Tarjeta) {
        viewModelScope.launch {
            try {
                deleteTarjetaUseCase.invoke(tarjeta)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.tarjeta_eliminada),
                    tarjetaAdded = null,
                    cleanFields = false,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.error_eliminar_tarjeta),
                    tarjetaAdded = null,
                    cleanFields = false,
                )
            }
        }
    }

    private fun clearState() {
        _uiState.value =
            _uiState.value?.copy(mensaje = null, tarjetaAdded = null, cleanFields = false)
    }

    private fun cleanFields() {
        _uiState.value = _uiState.value?.copy(
            mensaje = null,
            tarjetaAdded = null,
            cleanFields = true,
        )
    }

    fun handleEvent(event: AddTarjetaEvent) {
        when (event) {
            is AddTarjetaEvent.AddTarjeta -> addTarjeta(
                event.email,
                event.titular,
                event.numero,
                event.fechaCaducidad,
                event.cvv
            )
            is AddTarjetaEvent.ClearState -> clearState()
            is AddTarjetaEvent.CleanInputFields -> cleanFields()
            is AddTarjetaEvent.UndoAddTarjeta -> undoAddTarjeta(event.tarjeta)
        }
    }
}