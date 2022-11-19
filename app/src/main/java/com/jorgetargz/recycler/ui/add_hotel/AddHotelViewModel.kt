package com.jorgetargz.recycler.ui.add_hotel

import androidx.lifecycle.*
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.domain.modelo.Hotel
import com.jorgetargz.recycler.domain.usecases.hoteles.AddHotelUseCase
import com.jorgetargz.recycler.domain.usecases.hoteles.DeleteHotelUseCase
import com.jorgetargz.recycler.domain.usecases.hoteles.ValidarHotelUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AddHotelViewModel @Inject constructor(
    @Named(Constantes.NAMED_INJECT_STRING_PROVIDER)
    private val stringProvider: StringProvider,
    private val validarHotelUseCase: ValidarHotelUseCase,
    private val addHotelUseCase: AddHotelUseCase,
    private val deleteHotelUseCase: DeleteHotelUseCase,
) : ViewModel() {

    private val _uiState = MutableLiveData(
        AddHotelMainState(null)
    )

    val uiState: LiveData<AddHotelMainState> get() = _uiState

    private fun addHotel(cif: String, nombre: String, telefono: String, estrellas: String) {
        val errorValidacion =
            validarHotelUseCase(cif, nombre, telefono, estrellas, stringProvider)
        if (errorValidacion == null) {
            val hotel = Hotel(
                cif = cif,
                nombre = nombre,
                telefono = telefono,
                estrellas = estrellas.toInt(),
            )
            viewModelScope.launch {
                try {
                    addHotelUseCase.invoke(hotel)
                    _uiState.value = _uiState.value?.copy(
                        mensaje = stringProvider.getString(R.string.persona_añadida),
                        hotelAdded = hotel,
                        cleanFields = true,
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value?.copy(
                        mensaje = stringProvider.getString(R.string.error_añadir_persona),
                        hotelAdded = null,
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

    private fun undoAddHotel(hotel: Hotel) {
        viewModelScope.launch {
            try {
                deleteHotelUseCase.invoke(hotel)
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.persona_eliminada),
                    hotelAdded = null,
                    cleanFields = false,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    mensaje = stringProvider.getString(R.string.error_eliminar_persona),
                    hotelAdded = null,
                    cleanFields = false,
                )
            }
        }
    }

    private fun clearState() {
        _uiState.value =
            _uiState.value?.copy(mensaje = null, hotelAdded = null, cleanFields = false)
    }

    private fun cleanFields() {
        _uiState.value = _uiState.value?.copy(
            mensaje = null,
            hotelAdded = null,
            cleanFields = true,
        )
    }

    fun handleEvent(event: AddHotelEvent) {
        when (event) {
            is AddHotelEvent.AddHotel -> addHotel(
                event.cif,
                event.nombre,
                event.telefono,
                event.estrellas,
            )
            is AddHotelEvent.ClearState -> clearState()
            is AddHotelEvent.CleanInputFields -> cleanFields()
            is AddHotelEvent.UndoAddHotel -> undoAddHotel(event.hotel)
        }
    }
}