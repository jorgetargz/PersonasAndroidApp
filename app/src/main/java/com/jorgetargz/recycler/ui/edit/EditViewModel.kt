package com.jorgetargz.recycler.ui.edit

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.usecases.personas.GetPersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.UpdatePersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.ValidarPersonaUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class EditViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val validarPersonaUseCase: ValidarPersonaUseCase,
    private val getPersonaUseCase: GetPersonaUseCase,
    private val updatePersonaUseCase: UpdatePersonaUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        EditState(null, Persona(), null)
    )

    val uiState: LiveData<EditState> get() = _uiState

    private fun editPersona(email: String, nombre: String, telefono: String, fnacimiento: String) {
        val errorValidacion =
            validarPersonaUseCase(email, nombre, telefono, fnacimiento, stringProvider)
        if (errorValidacion == null) {
            val personaNueva = Persona(
                email,
                nombre,
                LocalDate.parse(
                    fnacimiento,
                    DateTimeFormatter.ofPattern(Constantes.DATE_FORMAT)
                ),
                telefono
            )
            viewModelScope.launch {
                val personaDatabase = getPersonaUseCase.invoke(email)
                try {
                    updatePersonaUseCase.invoke(personaNueva)
                    _uiState.value = _uiState.value?.copy(
                        mensaje = stringProvider.getString(R.string.persona_actualizada),
                        personaMostrar = personaNueva,
                        personaSinEditar = personaDatabase,
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value?.copy(
                        mensaje = stringProvider.getString(R.string.accion_fallida),
                    )
                }
            }
        } else {
            _uiState.value = _uiState.value?.copy(
                mensaje = errorValidacion,
            )
        }
    }

    private fun undoEditPersona(persona: Persona) {
        viewModelScope.launch {
            try {
                updatePersonaUseCase.invoke(persona)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.persona_actualizada),
                    personaSinEditar = null,
                    personaMostrar = persona,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.accion_fallida),
                )
            }
        }
    }

    private fun clearState() {
        _uiState.value = _uiState.value?.copy(mensaje = null, personaSinEditar = null)
    }

    private fun loadPerson(email: String) {
        viewModelScope.launch {
            val persona = getPersonaUseCase.invoke(email)
            _uiState.value = persona.let { p ->
                _uiState.value?.copy(
                    personaMostrar = p,
                )
            }
        }
    }

    fun handleEvent(event: EditEvent) {
        when (event) {
            is EditEvent.EditPersona -> editPersona(
                event.email,
                event.nombre,
                event.telefono,
                event.fnacimiento
            )
            is EditEvent.UndoEditPersona -> undoEditPersona(event.persona)
            is EditEvent.ClearState -> clearState()
            is EditEvent.LoadPersona -> loadPerson(event.email)
        }
    }
}