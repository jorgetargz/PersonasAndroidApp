package com.jorgetargz.recycler.ui.edit

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.usecases.personas.GetPersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.UpdatePersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.ValidarPersonaUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EditViewModel(
    private val stringProvider: StringProvider,
    private val validarPersonaUseCase: ValidarPersonaUseCase,
    private val getPersonaUseCase: GetPersonaUseCase,
    private val updatePersonaUseCase: UpdatePersonaUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        EditState(null, Persona(), false)
    )

    val uiState: LiveData<EditState> get() = _uiState

    private fun editPersona(email: String, nombre: String, telefono: String, fnacimiento: String) {
        val errorValidacion =
            validarPersonaUseCase(email, nombre, telefono, fnacimiento, stringProvider)
        if (errorValidacion == null) {
            _uiState.value = _uiState.value?.copy(
                mensaje = null,
                onEdit = true,
                persona = Persona(
                    email,
                    nombre,
                    LocalDate.parse(
                        fnacimiento,
                        DateTimeFormatter.ofPattern(Constantes.DATE_FORMAT)
                    ),
                    telefono
                ),
            )
        } else {
            _uiState.value = _uiState.value?.copy(
                mensaje = errorValidacion,
            )
        }
    }

    private fun confirmEditPersona(persona: Persona) {
        viewModelScope.launch {
            try {
                updatePersonaUseCase.invoke(persona)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.persona_actualizada),
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.accion_fallida),
                )
            }
        }
    }

    private fun clearState() {
        _uiState.value = _uiState.value?.copy(mensaje = null, onEdit = false)
    }

    private fun loadPerson(email: String) {
        viewModelScope.launch {
            val persona = getPersonaUseCase.invoke(email)
            _uiState.value = persona.let { p ->
                _uiState.value?.copy(
                    persona = p,
                )
            }
        }
    }

    fun handleEvent(event: EditEvent) {
        when (event) {
            is EditEvent.EditPersona -> editPersona(event.email, event.nombre, event.telefono, event.fnacimiento)
            is EditEvent.ConfirmEditPersona -> confirmEditPersona(event.persona)
            is EditEvent.ClearState -> clearState()
            is EditEvent.LoadPersona -> loadPerson(event.email)
        }
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class EditViewModelFactory(
    private val stringProvider: StringProvider,
    private val validarPersonaUseCase: ValidarPersonaUseCase,
    private val getPersonaUseCase: GetPersonaUseCase,
    private val updatePersonaUseCase: UpdatePersonaUseCase,

    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            @Suppress(Constantes.UNCHECKED_CAST)
            return EditViewModel(
                stringProvider,
                validarPersonaUseCase,
                getPersonaUseCase,
                updatePersonaUseCase,
            ) as T
        }
        throw IllegalArgumentException(Constantes.Unknown_ViewModel)
    }
}