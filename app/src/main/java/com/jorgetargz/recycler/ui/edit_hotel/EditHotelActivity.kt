package com.jorgetargz.recycler.ui.edit_hotel

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
import com.jorgetargz.recycler.databinding.ActivityEditHotelBinding
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.ui.common.loadUrl
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EditHotelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditHotelBinding
    private val stringProvider = StringProvider(this)

    private val viewModel: EditHotelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.handleEvent(EditHotelEvent.LoadHotel(intent.extras?.getString(Constantes.CIF)!!))

        with(binding) {
            containedButtonEditHotel.setOnClickListener {
                viewModel.handleEvent(
                    EditHotelEvent.EditHotel(
                        intent.getStringExtra(Constantes.CIF)!!,
                        binding.textFieldNombre.editText?.text.toString(),
                        binding.textFieldTelefono.editText?.text.toString(),
                        binding.textFieldEstrellas.editText?.text.toString(),
                    )
                )
            }

            imageViewSkyline.loadUrl(Constantes.IMAGE_SKYSCRAPERS)

        }

        viewModel.uiState.observe(this) { uiState ->
            uiState.mensaje?.let { mensaje ->
                Timber.i(mensaje)
                Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_LONG).show()
                clearTextFieldErrors()
                loadTextFieldErrors(mensaje)
                viewModel.handleEvent(EditHotelEvent.ClearState)
            }
            uiState.hotelMostrar.let {
                with(binding) {
                    textFieldCIF.editText?.setText(it.cif)
                    textFieldNombre.editText?.setText(it.nombre)
                    textFieldEstrellas.editText?.setText(it.estrellas.toString())
                    textFieldTelefono.editText?.setText(it.telefono)
                }
            }
            uiState.hotelSinEditar?.let { hotel ->
                Timber.i(Constantes.HOTEL_EDITED, hotel.cif)
                Snackbar.make(
                    binding.root,
                    stringProvider.getString(R.string.hotel_actualizado),
                    Snackbar.LENGTH_LONG
                ).setAction(stringProvider.getString(R.string.snackbar_undo)) {
                    viewModel.handleEvent(EditHotelEvent.UndoEditHotel(hotel))
                }.show()
                viewModel.handleEvent(EditHotelEvent.ClearState)
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
}
