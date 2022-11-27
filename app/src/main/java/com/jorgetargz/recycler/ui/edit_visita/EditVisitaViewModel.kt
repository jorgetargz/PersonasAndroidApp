package com.jorgetargz.recycler.ui.edit_visita

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Visita
import com.jorgetargz.recycler.domain.usecases.visitas.GetVisitByCIFAndEmailUseCase
import com.jorgetargz.recycler.domain.usecases.visitas.UpdateVisitUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class EditVisitaViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val getVisitByCIFAndEmailUseCase: GetVisitByCIFAndEmailUseCase,
    private val updateVisitUseCase: UpdateVisitUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        EditVisitaState(null, Visita(), null)
    )

    val uiState: LiveData<EditVisitaState> get() = _uiState

    private fun loadVisita(cif: String, email: String) {
        viewModelScope.launch {
            try {
                val visita = getVisitByCIFAndEmailUseCase.invoke(cif, email)!!
                _uiState.value = _uiState.value?.copy(
                    visitaMostrar = visita,
                )
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.accion_fallida))
            }
        }
    }

    private fun editVisita(cif: String, email: String, valoracion: String) {
        viewModelScope.launch {
            try {
                val visita = Visita(email, cif, valoracion)
                val visitaDB = getVisitByCIFAndEmailUseCase.invoke(cif, email)
                updateVisitUseCase.invoke(visita)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.visita_actualizada),
                    visitaMostrar = visita,
                    visitaSinEditar = visitaDB,
                )
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.accion_fallida),
                )
            }
        }
    }

    private fun undoEditVisita(visita: Visita) {
        viewModelScope.launch {
            try {
                updateVisitUseCase.invoke(visita)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.visita_actualizada),
                    visitaSinEditar = null,
                    visitaMostrar = visita,
                )
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.accion_fallida),
                )
            }
        }
    }

    private fun clearState() {
        _uiState.value = _uiState.value?.copy(mensaje = null, visitaSinEditar = null)
    }


    fun handleEvent(event: EditVisitaEvent) {
        when (event) {
            is EditVisitaEvent.EditVisita -> editVisita(
                event.cif,
                event.email,
                event.valoracion,
            )
            is EditVisitaEvent.UndoEditVisita -> undoEditVisita(event.visita)
            is EditVisitaEvent.ClearState -> clearState()
            is EditVisitaEvent.LoadVisita -> loadVisita(event.cif, event.email)
        }
    }
}