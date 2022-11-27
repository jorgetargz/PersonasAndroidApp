package com.jorgetargz.recycler.ui.edit_persona

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.usecases.personas.GetPersonaByEmailUseCase
import com.jorgetargz.recycler.domain.usecases.personas.UpdatePersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.ValidarPersonaUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class EditPersonaViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val validarPersonaUseCase: ValidarPersonaUseCase,
    private val getPersonaByEmailUseCase: GetPersonaByEmailUseCase,
    private val updatePersonaUseCase: UpdatePersonaUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        EditPersonaState(null, Persona(), null)
    )

    val uiState: LiveData<EditPersonaState> get() = _uiState

    private fun loadPerson(email: String) {
        viewModelScope.launch {
            try {
                val persona = getPersonaByEmailUseCase.invoke(email)
                _uiState.value = persona.let { p ->
                    _uiState.value?.copy(
                        personaMostrar = p,
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.accion_fallida),
                )
            }
        }
    }

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
                val personaDatabase = getPersonaByEmailUseCase.invoke(email)
                try {
                    updatePersonaUseCase.invoke(personaNueva)
                    _uiState.value = _uiState.value?.copy(
                        mensaje = stringProvider.getString(R.string.persona_actualizada),
                        personaMostrar = personaNueva,
                        personaSinEditar = personaDatabase,
                    )
                } catch (e: Exception) {
                    Timber.e(e)
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
                Timber.e(e)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.accion_fallida),
                )
            }
        }
    }

    private fun clearState() {
        _uiState.value = _uiState.value?.copy(mensaje = null, personaSinEditar = null)
    }

    fun handleEvent(event: EditPersonaEvent) {
        when (event) {
            is EditPersonaEvent.EditPersona -> editPersona(
                event.email,
                event.nombre,
                event.telefono,
                event.fnacimiento
            )
            is EditPersonaEvent.UndoEditPersona -> undoEditPersona(event.persona)
            is EditPersonaEvent.ClearState -> clearState()
            is EditPersonaEvent.LoadPersona -> loadPerson(event.email)
        }
    }
}