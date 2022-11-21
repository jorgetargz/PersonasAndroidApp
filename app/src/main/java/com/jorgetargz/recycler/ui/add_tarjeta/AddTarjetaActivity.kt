package com.jorgetargz.recycler.ui.add_tarjeta

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.databinding.ActivityAddTarjetaBinding
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.ui.common.loadUrl
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class AddTarjetaActivity : AppCompatActivity() {

    private var temp: Int = 0
    private lateinit var binding: ActivityAddTarjetaBinding
    private val stringProvider = StringProvider(this)
    private var email: String? = null

    private val viewModel: AddTarjetaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTarjetaBinding.inflate(layoutInflater)

        with(binding) {
            setContentView(root)

            imageViewTarjetas.loadUrl(Constantes.IMAGE_TAJETA)

            email = intent.extras?.getString(Constantes.EMAIL)

            containedButtonAddTarjeta.setOnClickListener {
                viewModel.handleEvent(
                    AddTarjetaEvent.AddTarjeta(
                        email!!,
                        textFieldTitular.editText?.text.toString(),
                        textFieldNumero.editText?.text.toString(),
                        textFieldCaducidad.editText?.text.toString(),
                        textFieldCVV.editText?.text.toString(),
                    )
                )
            }

            containedButtonClean.setOnClickListener {
                viewModel.handleEvent(AddTarjetaEvent.CleanInputFields)
            }

            containedButtonSelectDate.setOnClickListener {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText(stringProvider.getString(R.string.date_picker_title))
                        .build()
                datePicker.addOnPositiveButtonClickListener { selection ->
                    val zoneId: ZoneId = ZoneId.systemDefault()
                    val formatter = DateTimeFormatter.ofPattern(Constantes.DATE_FORMAT)
                    val selectedDate = Date(selection)
                    val formattedDate = selectedDate.toInstant()
                        .atZone(zoneId)
                        .toLocalDate()
                        .format(formatter)
                    textFieldCaducidad.editText?.setText(formattedDate)
                }
                datePicker.show(supportFragmentManager, Constantes.DATE_PICKER_TAG)
            }
        }

        viewModel.uiState.observe(this) { state ->
            state.mensaje?.let { mensaje ->
                Timber.i(mensaje)
                Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT).show()
                clearTextFieldErrors()
                loadTextFieldErrors(mensaje)
                viewModel.handleEvent(AddTarjetaEvent.ClearState)

            }
            state.tarjetaAdded?.let { tarjeta ->
                Timber.i(Constantes.TARJETA_ADDED)
                Snackbar.make(
                    binding.root,
                    stringProvider.getString(R.string.tarjeta_añadida),
                    Snackbar.LENGTH_LONG
                ).setAction(stringProvider.getString(R.string.snackbar_undo)) {
                    viewModel.handleEvent(AddTarjetaEvent.UndoAddTarjeta(tarjeta))
                }.show()
                viewModel.handleEvent(AddTarjetaEvent.ClearState)
            }
            if (state.cleanFields) {
                clearTextFieldErrors()
                clearTextFields()
                viewModel.handleEvent(AddTarjetaEvent.ClearState)
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
            stringProvider.getString(R.string.error_nombre_titular_tarjeta) -> {
                binding.textFieldTitular.error = error
            }
            stringProvider.getString(R.string.error_numero_tarjeta) -> {
                binding.textFieldNumero.error = error
            }
            stringProvider.getString(R.string.error_fecha_caducidad_tarjeta) -> {
                binding.textFieldCaducidad.error = error
            }
            stringProvider.getString(R.string.error_cvv_tarjeta) -> {
                binding.textFieldCVV.error = error
            }
        }
    }

    private fun clearTextFieldErrors() {
        binding.textFieldTitular.error = null
        binding.textFieldNumero.error = null
        binding.textFieldCVV.error = null
        binding.textFieldCaducidad.error = null
    }

    private fun clearTextFields() {
        binding.textFieldTitular.editText?.setText(Constantes.EMPTY_STRING)
        binding.textFieldNumero.editText?.setText(Constantes.EMPTY_STRING)
        binding.textFieldCVV.editText?.setText(Constantes.EMPTY_STRING)
        binding.textFieldCaducidad.editText?.setText(Constantes.EMPTY_STRING)
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

