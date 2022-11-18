package com.jorgetargz.recycler.ui.listado

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.usecases.personas.AddPersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.DeletePersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.GetPersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.GetPersonasUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val getPersonasUseCase: GetPersonasUseCase,
    private val deletePersonaUseCase: DeletePersonaUseCase,
    private val addPersonaUseCase: AddPersonaUseCase,
    private val getPersonaUseCase: GetPersonaUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        ListState(null, null, null)
    )

    val uiState: LiveData<ListState> = _uiState

    private fun clearState() {
        _uiState.value = _uiState.value?.copy(mensaje = null, personaDeleted = null)
    }

    private fun deletePerson(email: String) {
        viewModelScope.launch {
            try {
                val persona = getPersonaUseCase.invoke(email)
                deletePersonaUseCase.invoke(persona)
                _uiState.value = _uiState.value?.copy(
                    personaDeleted = persona,
                )
                loadPersonas()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(mensaje = e.message)
            }
        }
    }

    private fun undoDelete(persona: Persona) {
        viewModelScope.launch {
            try {
                addPersonaUseCase.invoke(persona)
                _uiState.value = _uiState.value?.copy(
                    personaDeleted = null,
                    mensaje = stringProvider.getString(R.string.undo_delete)
                )
                loadPersonas()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(mensaje = e.message)
            }
        }
    }

    private fun loadPersonas() {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(
                lista = getPersonasUseCase.invoke(),
            )
        }
    }

    fun handleEvent(event: ListEvent) {
        when (event) {
            is ListEvent.DeletePersona -> deletePerson(event.email)
            is ListEvent.UndoDeletePersona -> undoDelete(event.persona)
            is ListEvent.LoadPersonas -> loadPersonas()
            is ListEvent.ClearState -> clearState()
        }
    }
}