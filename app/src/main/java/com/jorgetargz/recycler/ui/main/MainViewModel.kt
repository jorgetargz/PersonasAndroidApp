package com.jorgetargz.recycler.ui.main

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.usecases.personas.AddPersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.ValidarPersonaUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import kotlinx.coroutines.launch

class MainViewModel(
    private val stringProvider: StringProvider,
    private val validarPersonaUseCase: ValidarPersonaUseCase,
    private val addPersonaUseCase: AddPersonaUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        MainState(null)
    )

    val uiState: LiveData<MainState> get() = _uiState

    private fun addPersona(email: String, nombre: String, telefono: String, fnacimiento: String) {
        val errorValidacion =
            validarPersonaUseCase(email, nombre, telefono, fnacimiento, stringProvider)
        if (errorValidacion == null) {
            _uiState.value = _uiState.value?.copy(
                mensaje = null,
                onAdd = true,
                cleanFields = false,
            )
        } else {
            _uiState.value = _uiState.value?.copy(
                mensaje = errorValidacion,
            )
        }
    }

    private fun confirmAddPersona(persona: Persona) {
        viewModelScope.launch {
            try {
                addPersonaUseCase.invoke(persona)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.persona_añadida),
                    onAdd = false,
                    cleanFields = true,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.error_añadir_persona),
                    onAdd = false,
                    cleanFields = true,
                )
            }
        }
    }

    private fun clearState() {
        _uiState.value = _uiState.value?.copy(mensaje = null, onAdd = false, cleanFields = false)
    }

    private fun cleanFields() {
        _uiState.value = _uiState.value?.copy(
            mensaje = null,
            onAdd = false,
            cleanFields = true,
        )
    }

    fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.AddPersona -> addPersona(
                event.email,
                event.nombre,
                event.telefono,
                event.fnacimiento
            )
            is MainEvent.ConfirmAddPersona -> confirmAddPersona(event.persona)
            is MainEvent.ClearState -> clearState()
            is MainEvent.CleanInputFields -> cleanFields()
        }
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class MainViewModelFactory(
    private val stringProvider: StringProvider,
    private val validarPersonaUseCase: ValidarPersonaUseCase,
    private val addPersonaUseCase: AddPersonaUseCase,

    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress(Constantes.UNCHECKED_CAST)
            return MainViewModel(
                stringProvider,
                validarPersonaUseCase,
                addPersonaUseCase,
            ) as T
        }
        throw IllegalArgumentException(Constantes.Unknown_ViewModel)
    }
}