package com.jorgetargz.recycler.ui.add_persona

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.usecases.personas.AddPersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.DeletePersonaUseCase
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
class AddPersonaViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val validarPersonaUseCase: ValidarPersonaUseCase,
    private val addPersonaUseCase: AddPersonaUseCase,
    private val deletePersonaUseCase: DeletePersonaUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        AddPersonaMainState(null)
    )

    val uiState: LiveData<AddPersonaMainState> get() = _uiState

    private fun addPersona(email: String, nombre: String, telefono: String, fnacimiento: String) {
        val errorValidacion =
            validarPersonaUseCase(email, nombre, telefono, fnacimiento, stringProvider)
        if (errorValidacion == null) {
            val persona = Persona(
                email = email,
                nombre = nombre,
                telefono = telefono,
                fnacimiento = LocalDate.parse(
                    fnacimiento, DateTimeFormatter.ofPattern(Constantes.DATE_FORMAT)
                ),
            )
            viewModelScope.launch {
                try {
                    addPersonaUseCase.invoke(persona)
                    _uiState.value = _uiState.value?.copy(
                        mensaje = stringProvider.getString(R.string.persona_añadida),
                        personaAdded = persona,
                        cleanFields = true,
                    )
                } catch (e: Exception) {
                    Timber.e(e)
                    _uiState.value = _uiState.value?.copy(
                        mensaje = stringProvider.getString(R.string.error_añadir_persona),
                        personaAdded = null,
                        cleanFields = false,
                    )
                }
            }
        } else {
            _uiState.value = _uiState.value?.copy(
                mensaje = errorValidacion,
            )
        }
    }

    private fun undoAddPersona(persona: Persona) {
        viewModelScope.launch {
            try {
                deletePersonaUseCase.invoke(persona)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.persona_eliminada),
                    personaAdded = null,
                    cleanFields = false,
                )
            } catch (e: Exception) {
                Timber.e(e)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.error_eliminar_persona),
                    personaAdded = null,
                    cleanFields = false,
                )
            }
        }
    }

    private fun clearState() {
        _uiState.value =
            _uiState.value?.copy(mensaje = null, personaAdded = null, cleanFields = false)
    }

    private fun cleanFields() {
        _uiState.value = _uiState.value?.copy(
            mensaje = null,
            personaAdded = null,
            cleanFields = true,
        )
    }

    fun handleEvent(event: AddPersonaEvent) {
        when (event) {
            is AddPersonaEvent.AddPersona -> addPersona(
                event.email,
                event.nombre,
                event.telefono,
                event.fnacimiento,
            )
            is AddPersonaEvent.ClearState -> clearState()
            is AddPersonaEvent.CleanInputFields -> cleanFields()
            is AddPersonaEvent.UndoAddPersona -> undoAddPersona(event.persona)
        }
    }
}