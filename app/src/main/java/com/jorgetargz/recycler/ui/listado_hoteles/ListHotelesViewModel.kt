package com.jorgetargz.recycler.ui.listado_hoteles

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Hotel
import com.jorgetargz.recycler.domain.usecases.hoteles.AddHotelUseCase
import com.jorgetargz.recycler.domain.usecases.hoteles.DeleteHotelUseCase
import com.jorgetargz.recycler.domain.usecases.hoteles.GetHotelByCIF
import com.jorgetargz.recycler.domain.usecases.hoteles.GetHotelesUseCase
import com.jorgetargz.recycler.domain.usecases.personas.GetHotelesByPersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.GetPersonaByEmailUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListHotelesViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val getHotelesUseCase: GetHotelesUseCase,
    private val getPersonaByEmailUseCase: GetPersonaByEmailUseCase,
    private val getHotelesByPersonaUseCase: GetHotelesByPersonaUseCase,
    private val getHotelByCIF: GetHotelByCIF,
    private val addHotelUseCase: AddHotelUseCase,
    private val deleteHotelUseCase: DeleteHotelUseCase,
) : ViewModel() {
    private val _uiState = MutableLiveData(
        ListHotelesState(null, null, null)
    )

    val uiState: LiveData<ListHotelesState> = _uiState

    private fun clearState() {
        _uiState.value = _uiState.value?.copy(mensaje = null, hotelDeleted = null)
    }

    private fun loadHoteles() {
        try {
            viewModelScope.launch {
                _uiState.value = _uiState.value?.copy(
                    lista = getHotelesUseCase.invoke(),
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value?.copy(mensaje = e.message)
        }
    }


    private fun loadHotelesByPersonaEmail(email: String) {
        try {
            viewModelScope.launch {
                val persona = getPersonaByEmailUseCase.invoke(email)
                _uiState.value = _uiState.value?.copy(
                    lista = getHotelesByPersonaUseCase.invoke(persona),
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value?.copy(mensaje = e.message)
        }
    }


    private fun deleteHotel(cif: String) {
        viewModelScope.launch {
            try {
                val hotel = getHotelByCIF.invoke(cif)
                deleteHotelUseCase.invoke(hotel)
                _uiState.value = _uiState.value?.copy(
                    hotelDeleted = hotel,
                )
                loadHoteles()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(mensaje = e.message)
            }
        }
    }

    private fun undoDelete(hotel: Hotel) {
        viewModelScope.launch {
            try {
                addHotelUseCase.invoke(hotel)
                _uiState.value = _uiState.value?.copy(
                    hotelDeleted = null,
                    mensaje = stringProvider.getString(R.string.undo_delete_hotel)
                )
                loadHoteles()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(mensaje = e.message)
            }
        }
    }

    fun handleEvent(event: ListHotelesEvent) {
        when (event) {
            is ListHotelesEvent.DeleteHotel -> deleteHotel(event.cif)
            is ListHotelesEvent.UndoDeleteHotel -> undoDelete(event.hotel)
            is ListHotelesEvent.LoadHoteles -> loadHoteles()
            is ListHotelesEvent.ClearState -> clearState()
            is ListHotelesEvent.LoadHotelesByPersonaEmail -> loadHotelesByPersonaEmail(event.email)
        }
    }
}