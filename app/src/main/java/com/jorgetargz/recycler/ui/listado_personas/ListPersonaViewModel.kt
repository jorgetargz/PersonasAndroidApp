package com.jorgetargz.recycler.ui.listado_personas

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.modelo.Tarjeta
import com.jorgetargz.recycler.domain.usecases.hoteles.GetHotelByCIF
import com.jorgetargz.recycler.domain.usecases.hoteles.GetPersonasByHotelUseCase
import com.jorgetargz.recycler.domain.usecases.personas.*
import com.jorgetargz.recycler.domain.usecases.tarjetas.AddTarjetaUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListPersonaViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val getPersonasUseCase: GetPersonasUseCase,
    private val deletePersonaUseCase: DeletePersonaUseCase,
    private val addPersonaUseCase: AddPersonaUseCase,
    private val getPersonaByEmailUseCase: GetPersonaByEmailUseCase,
    private val getPersonasByHotelUseCase: GetPersonasByHotelUseCase,
    private val getHotelByCIF: GetHotelByCIF,
    private val getTarjetasByPersonaUseCase: GetTarjetasByPersonaUseCase,
    private val addTarjetaUseCase: AddTarjetaUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        ListPersonaState(null, null, null, null)
    )

    val uiState: LiveData<ListPersonaState> = _uiState

    private fun clearState() {
        _uiState.value = _uiState.value?.copy(mensaje = null, personaDeleted = null)
    }

    private fun deletePerson(email: String) {
        viewModelScope.launch {
            try {
                val persona = getPersonaByEmailUseCase.invoke(email)
                val tarjetas = getTarjetasByPersonaUseCase.invoke(persona)
                deletePersonaUseCase.invoke(persona)
                _uiState.value = _uiState.value?.copy(
                    tarjetasDeleted = tarjetas,
                    personaDeleted = persona,
                )
                loadPersonas()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(mensaje = e.message)
            }
        }
    }

    private fun undoDelete(persona: Persona, tarjetas: List<Tarjeta>?) {
        viewModelScope.launch {
            try {
                addPersonaUseCase.invoke(persona)
                tarjetas?.forEach { addTarjetaUseCase.invoke(it) }
                _uiState.value = _uiState.value?.copy(
                    personaDeleted = null,
                    mensaje = stringProvider.getString(R.string.undo_delete_persona)
                )
                loadPersonas()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(mensaje = e.message)
            }
        }
    }

    private fun loadPersonas() {
        try {
            viewModelScope.launch {
                _uiState.value = _uiState.value?.copy(
                    lista = getPersonasUseCase.invoke(),
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value?.copy(mensaje = e.message)
        }
    }

    private fun loadPersonasByHotelCIF(cifHotel: String) {
        try {
            viewModelScope.launch {
                val hotel = getHotelByCIF.invoke(cifHotel)
                _uiState.value = _uiState.value?.copy(
                    lista = getPersonasByHotelUseCase.invoke(hotel),
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value?.copy(mensaje = e.message)
        }
    }

    fun handleEvent(event: ListPersonaEvent) {
        when (event) {
            is ListPersonaEvent.DeletePersona -> deletePerson(event.email)
            is ListPersonaEvent.UndoDeletePersona -> undoDelete(event.persona, event.tarjetas)
            is ListPersonaEvent.LoadPersonas -> loadPersonas()
            is ListPersonaEvent.ClearState -> clearState()
            is ListPersonaEvent.LoadPersonasByHotelCif -> loadPersonasByHotelCIF(event.cifHotel)
        }
    }
}