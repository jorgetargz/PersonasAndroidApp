package com.jorgetargz.recycler.ui.add_visita

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Visita
import com.jorgetargz.recycler.domain.usecases.hoteles.GetHotelesUseCase
import com.jorgetargz.recycler.domain.usecases.personas.GetPersonasUseCase
import com.jorgetargz.recycler.domain.usecases.visitas.AddVisitUseCase
import com.jorgetargz.recycler.domain.usecases.visitas.DeleteVisitUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AddVisitaViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val addVisitUseCase: AddVisitUseCase,
    private val deleteVisitUseCase: DeleteVisitUseCase,
    private val getHotelesUseCase: GetHotelesUseCase,
    private val getPersonasUseCase: GetPersonasUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        AddVisitaState(null)
    )

    val uiState: LiveData<AddVisitaState> get() = _uiState

    private fun addVisita(cif: String, email: String, valoracion: String) {
        if (cif.isEmpty() || email.isEmpty() || valoracion.isEmpty()) {
            _uiState.value = AddVisitaState(
                mensaje = stringProvider.getString(R.string.rellena_campos)
            )
        } else {
            val visita = Visita(email, cif, valoracion)
            viewModelScope.launch {
                try {
                    addVisitUseCase.invoke(visita)
                    _uiState.value = _uiState.value?.copy(
                        mensaje = stringProvider.getString(R.string.visita_añadida),
                        visitaAdded = visita,
                        cleanFields = true,
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value?.copy(
                        mensaje = stringProvider.getString(R.string.error_añadir_visita),
                        visitaAdded = null,
                        cleanFields = false,
                    )
                }
            }
        }
    }

    private fun undoAddVisita(visita: Visita) {
        viewModelScope.launch {
            try {
                deleteVisitUseCase.invoke(visita)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.visita_eliminada),
                    visitaAdded = null,
                    cleanFields = false,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.error_eliminar_visita),
                    visitaAdded = null,
                    cleanFields = false,
                )
            }
        }
    }

    private fun loadDropdownsLists() {
        try {
            viewModelScope.launch {
                val hotelCIFS = getHotelesUseCase.invoke().map { it.cif }
                val emailsPersonas = getPersonasUseCase.invoke().map { it.email }
                _uiState.value = _uiState.value?.copy(
                    cifList = hotelCIFS,
                    emailList = emailsPersonas,
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value?.copy(
                mensaje = stringProvider.getString(R.string.error_cargar_listas),
            )
        }
    }

    private fun clearState() {
        _uiState.value =
            _uiState.value?.copy(mensaje = null, visitaAdded = null, cleanFields = false)
    }

    private fun cleanFields() {
        _uiState.value = _uiState.value?.copy(
            mensaje = null,
            visitaAdded = null,
            cleanFields = true,
        )
    }

    fun handleEvent(event: AddVisitaEvent) {
        when (event) {
            is AddVisitaEvent.AddVisita -> addVisita(
                event.cif,
                event.email,
                event.valoracion
            )
            is AddVisitaEvent.ClearState -> clearState()
            is AddVisitaEvent.CleanInputFields -> cleanFields()
            is AddVisitaEvent.UndoAddVisita -> undoAddVisita(event.visita)
            is AddVisitaEvent.LoadDropdownLists -> loadDropdownsLists()
        }
    }
}