package com.jorgetargz.recycler.ui.add_hotel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.databinding.ActivityAddHotelBinding
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.ui.common.loadUrl
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddHotelActivity : AppCompatActivity() {

    private var temp: Int = 0
    private lateinit var binding: ActivityAddHotelBinding
    private val stringProvider = StringProvider(this)


    private val viewModel: AddHotelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHotelBinding.inflate(layoutInflater)

        with(binding) {
            setContentView(root)

            imageViewPersonas.loadUrl(Constantes.IMAGE_SKYSCRAPERS)

            containedButtonAddPerson.setOnClickListener {
                viewModel.handleEvent(
                    AddHotelEvent.AddHotel(
                        textFieldCIF.editText?.text.toString(),
                        textFieldNombre.editText?.text.toString(),
                        textFieldTelefono.editText?.text.toString(),
                        textFieldEstrellas.editText?.text.toString(),
                    )
                )
            }

            containedButtonClean.setOnClickListener {
                viewModel.handleEvent(AddHotelEvent.CleanInputFields)
            }
        }

        viewModel.uiState.observe(this) { state ->
            state.mensaje?.let { mensaje ->
                Timber.i(mensaje)
                Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT).show()
                clearTextFieldErrors()
                loadTextFieldErrors(mensaje)
                viewModel.handleEvent(AddHotelEvent.ClearState)

            }
            state.hotelAdded?.let { hotel ->
                Timber.i(Constantes.HOTEL_ADDED, hotel.cif)
                Snackbar.make(
                    binding.root,
                    stringProvider.getString(R.string.hotel_añadido),
                    Snackbar.LENGTH_LONG
                ).setAction(stringProvider.getString(R.string.snackbar_undo)) {
                    viewModel.handleEvent(AddHotelEvent.UndoAddHotel(hotel))
                }.show()
                viewModel.handleEvent(AddHotelEvent.ClearState)
            }
            if (state.cleanFields) {
                clearTextFieldErrors()
                clearTextFields()
                viewModel.handleEvent(AddHotelEvent.ClearState)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.acerca_de -> {
                val dialog = MaterialAlertDialogBuilder(this)
                    .setTitle(stringProvider.getString(R.string.dialog_acerca_de_title))
                    .setMessage(stringProvider.getString(R.string.dialog_acerca_de_content))
                    .setPositiveButton(stringProvider.getString(R.string.dialog_dismiss)) { view, _ ->
                        view.dismiss()
                    }
                    .setCancelable(true)
                    .create()
                dialog.show()
                true
            }
            R.id.github -> {
                val myProfileURI = Uri.parse(Constantes.GITHUB_PROFILE_URL)
                val intent = Intent(Intent.ACTION_VIEW, myProfileURI)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun loadTextFieldErrors(error: String) {
        when (error) {
            stringProvider.getString(R.string.cif_incorrecto) -> {
                binding.textFieldCIF.error = error
            }
            stringProvider.getString(R.string.nombre_incorrecto) -> {
                binding.textFieldNombre.error = error
            }
            stringProvider.getString(R.string.telefono_incorrecto) -> {
                binding.textFieldTelefono.error = error
            }
            stringProvider.getString(R.string.estrellas_incorrecta) -> {
                binding.textFieldEstrellas.error = error
            }
        }
    }

    private fun clearTextFieldErrors() {
        binding.textFieldCIF.error = null
        binding.textFieldNombre.error = null
        binding.textFieldTelefono.error = null
        binding.textFieldEstrellas.error = null
    }

    private fun clearTextFields() {
        binding.textFieldCIF.editText?.setText(Constantes.EMPTY_STRING)
        binding.textFieldNombre.editText?.setText(Constantes.EMPTY_STRING)
        binding.textFieldEstrellas.editText?.setText(Constantes.EMPTY_STRING)
        binding.textFieldTelefono.editText?.setText(Constantes.EMPTY_STRING)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.tag(Constantes.INSTANCE).i(Constantes.ON_SAVE_INSTANCE_STATE)
        outState.putInt(Constantes.COUNT_KEY, temp)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.tag(Constantes.INSTANCE).i(Constantes.ON_RESTORE_INSTANCE_STATE)
        temp = savedInstanceState.getInt(Constantes.COUNT_KEY)
    }
}

