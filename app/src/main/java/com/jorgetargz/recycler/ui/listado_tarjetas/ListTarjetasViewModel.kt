package com.jorgetargz.recycler.ui.listado_tarjetas

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Tarjeta
import com.jorgetargz.recycler.domain.usecases.personas.*
import com.jorgetargz.recycler.domain.usecases.tarjetas.AddTarjetaUseCase
import com.jorgetargz.recycler.domain.usecases.tarjetas.DeleteTarjetaUseCase
import com.jorgetargz.recycler.domain.usecases.tarjetas.GetTarjetaByNumeroUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListTarjetasViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val getTarjetaByNumeroUseCase: GetTarjetaByNumeroUseCase,
    private val getPersonaByEmailUseCase: GetPersonaByEmailUseCase,
    private val getTarjetasByPersonaUseCase: GetTarjetasByPersonaUseCase,
    private val deleteTarjetaUseCase: DeleteTarjetaUseCase,
    private val addTarjetaUseCase: AddTarjetaUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        ListTarjetasState(null, null, null)
    )

    val uiState: LiveData<ListTarjetasState> = _uiState

    private fun clearState() {
        _uiState.value = _uiState.value?.copy(mensaje = null, tarjetaDeleted = null)
    }

    private fun deleteTarjeta(numero: String) {
        viewModelScope.launch {
            try {
                val tarjeta = getTarjetaByNumeroUseCase.invoke(numero)
                deleteTarjetaUseCase.invoke(tarjeta)
                _uiState.value = _uiState.value?.copy(
                    tarjetaDeleted = tarjeta,
                )
                loadTarjetasByEmail(tarjeta.emailPersona)
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.value = _uiState.value?.copy(mensaje = e.message)
            }
        }
    }

    private fun undoDelete(tarjeta: Tarjeta) {
        viewModelScope.launch {
            try {
                addTarjetaUseCase.invoke(tarjeta)
                _uiState.value = _uiState.value?.copy(
                    tarjetaDeleted = null,
                    mensaje = stringProvider.getString(R.string.undo_delete_tarjeta)
                )
                loadTarjetasByEmail(tarjeta.emailPersona)
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.value = _uiState.value?.copy(mensaje = e.message)
            }
        }
    }


    private fun loadTarjetasByEmail(email: String) {
        try {
            viewModelScope.launch {
                val persona = getPersonaByEmailUseCase.invoke(email)
                _uiState.value = _uiState.value?.copy(
                    lista = getTarjetasByPersonaUseCase.invoke(persona),
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
            _uiState.value = _uiState.value?.copy(mensaje = e.message)
        }
    }

    fun handleEvent(event: ListTarjetasEvent) {
        when (event) {
            is ListTarjetasEvent.DeleteTarjeta -> deleteTarjeta(event.numero)
            is ListTarjetasEvent.UndoDeleteTarjeta -> undoDelete(event.tarjeta)
            is ListTarjetasEvent.ClearState -> clearState()
            is ListTarjetasEvent.LoadTarjetasByEmail -> loadTarjetasByEmail(event.email)
        }
    }
}