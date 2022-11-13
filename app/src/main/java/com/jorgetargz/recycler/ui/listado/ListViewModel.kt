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
import kotlinx.coroutines.launch

class ListViewModel(
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
        _uiState.value = _uiState.value?.copy(mensaje = null, onDelete = null)
    }

    private fun deletePerson(email: String) {
        viewModelScope.launch {
            try {
                val persona = getPersonaUseCase.invoke(email)
                deletePersonaUseCase.invoke(persona)
                _uiState.value = _uiState.value?.copy(
                    onDelete = persona,
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

    private fun undoDelete(persona: Persona) {
        viewModelScope.launch {
            try {
                addPersonaUseCase.invoke(persona)
                _uiState.value = _uiState.value?.copy(
                    onDelete = null,
                    mensaje = stringProvider.getString(R.string.undo_delete)
                )
                loadPersonas()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(mensaje = e.message)
            }
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

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class ListViewModelFactory(
    private val stringProvider: StringProvider,
    private val getPersonasUseCase: GetPersonasUseCase,
    private val deletePersonaUseCase: DeletePersonaUseCase,
    private val addPersonaUseCase: AddPersonaUseCase,
    private val getPersonaUseCase: GetPersonaUseCase,


    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            @Suppress(Constantes.UNCHECKED_CAST)
            return ListViewModel(
                stringProvider,
                getPersonasUseCase,
                deletePersonaUseCase,
                addPersonaUseCase,
                getPersonaUseCase,
            ) as T
        }
        throw IllegalArgumentException(Constantes.Unknown_ViewModel)
    }
}