package com.jorgetargz.recycler.ui.listado_visitas

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Visita
import com.jorgetargz.recycler.domain.usecases.visitas.*
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListVisitasViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val getVisitsUseCase: GetVisitsUseCase,
    private val getVisitByCIFAndEmailUseCase: GetVisitByCIFAndEmailUseCase,
    private val getVisitsByHotelCIFUseCase: GetVisitsByHotelCIFUseCase,
    private val getVisitsByPersonEmailUseCase: GetVisitsByPersonEmailUseCase,
    private val deleteVisitUseCase: DeleteVisitUseCase,
    private val addVisitUseCase: AddVisitUseCase,
) : ViewModel() {
    private val _uiState = MutableLiveData(
        ListVisitasState(null, null, null)
    )

    val uiState: LiveData<ListVisitasState> = _uiState

    private fun clearState() {
        _uiState.value = _uiState.value?.copy(mensaje = null, visitaDeleted = null)
    }

    private fun loadVisitas() {
        try {
            viewModelScope.launch {
                _uiState.value = _uiState.value?.copy(
                    lista = getVisitsUseCase.invoke(),
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value?.copy(mensaje = e.message)
        }
    }


    private fun loadVisitsByPersonaEmail(email: String) {
        try {
            viewModelScope.launch {
                _uiState.value = _uiState.value?.copy(
                    lista = getVisitsByPersonEmailUseCase.invoke(email),
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value?.copy(mensaje = e.message)
        }
    }

    private fun loadVisitsByHotelCIF(cif: String) {
        try {
            viewModelScope.launch {
                _uiState.value = _uiState.value?.copy(
                    lista = getVisitsByHotelCIFUseCase.invoke(cif),
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value?.copy(mensaje = e.message)
        }
    }


    private fun deleteVisita(cif: String, email: String) {
        viewModelScope.launch {
            try {
                val visita = getVisitByCIFAndEmailUseCase.invoke(cif, email)
                deleteVisitUseCase.invoke(visita!!)
                _uiState.value = _uiState.value?.copy(
                    visitaDeleted = visita,
                )
                loadVisitas()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(mensaje = e.message)
            }
        }
    }

    private fun undoDelete(visita: Visita) {
        viewModelScope.launch {
            try {
                addVisitUseCase.invoke(visita)
                _uiState.value = _uiState.value?.copy(
                    visitaDeleted = null,
                    mensaje = stringProvider.getString(R.string.undo_delete_visita)
                )
                loadVisitas()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(mensaje = e.message)
            }
        }
    }

    fun handleEvent(event: ListVisitasEvent) {
        when (event) {
            is ListVisitasEvent.DeleteVisita -> deleteVisita(event.cif, event.email)
            is ListVisitasEvent.UndoDeleteVisita -> undoDelete(event.visita)
            is ListVisitasEvent.LoadVisitas -> loadVisitas()
            is ListVisitasEvent.ClearState -> clearState()
            is ListVisitasEvent.LoadVisitasByPersonaEmail -> loadVisitsByPersonaEmail(event.email)
            is ListVisitasEvent.LoadVisitasByHotelCIF -> loadVisitsByHotelCIF(event.cif)
        }
    }
}