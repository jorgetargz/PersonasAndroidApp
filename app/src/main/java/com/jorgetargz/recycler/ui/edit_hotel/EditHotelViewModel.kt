package com.jorgetargz.recycler.ui.edit_hotel

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Hotel
import com.jorgetargz.recycler.domain.usecases.hoteles.GetHotelByCIF
import com.jorgetargz.recycler.domain.usecases.hoteles.UpdateHotelUseCase
import com.jorgetargz.recycler.domain.usecases.hoteles.ValidarHotelUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class EditHotelViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val validarHotelUseCase: ValidarHotelUseCase,
    private val getHotelByCIF: GetHotelByCIF,
    private val updateHotelUseCase: UpdateHotelUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        EditHotelState(null, Hotel(), null)
    )

    val uiState: LiveData<EditHotelState> get() = _uiState

    private fun editHotel(cif: String, nombre: String, telefono: String, estrellas: String) {
        val errorValidacion =
            validarHotelUseCase(cif, nombre, telefono, estrellas, stringProvider)
        if (errorValidacion == null) {
            val hotelNuevo = Hotel(
                cif,
                nombre,
                estrellas.toInt(),
                telefono
            )
            viewModelScope.launch {
                val personaDatabase = getHotelByCIF.invoke(cif)
                try {
                    updateHotelUseCase.invoke(hotelNuevo)
                    _uiState.value = _uiState.value?.copy(
                        mensaje = stringProvider.getString(R.string.hotel_actualizado),
                        hotelMostrar = hotelNuevo,
                        hotelSinEditar = personaDatabase,
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

    private fun undoEditHotel(hotel: Hotel) {
        viewModelScope.launch {
            try {
                updateHotelUseCase.invoke(hotel)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.hotel_actualizado),
                    hotelSinEditar = null,
                    hotelMostrar = hotel,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.accion_fallida),
                )
            }
        }
    }

    private fun clearState() {
        _uiState.value = _uiState.value?.copy(mensaje = null, hotelSinEditar = null)
    }

    private fun loadPerson(cif: String) {
        viewModelScope.launch {
            val persona = getHotelByCIF.invoke(cif)
            _uiState.value = persona.let { p ->
                _uiState.value?.copy(
                    hotelMostrar = p,
                )
            }
        }
    }

    fun handleEvent(event: EditHotelEvent) {
        when (event) {
            is EditHotelEvent.EditHotel -> editHotel(
                event.cif,
                event.nombre,
                event.telefono,
                event.estrellas
            )
            is EditHotelEvent.UndoEditHotel -> undoEditHotel(event.hotel)
            is EditHotelEvent.ClearState -> clearState()
            is EditHotelEvent.LoadHotel -> loadPerson(event.cif)
        }
    }
}