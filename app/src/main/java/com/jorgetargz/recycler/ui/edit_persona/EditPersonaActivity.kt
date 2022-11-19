package com.jorgetargz.recycler.ui.edit_persona

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
import com.jorgetargz.recycler.databinding.ActivityEditPersonaBinding
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.ui.common.loadUrl
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class EditPersonaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPersonaBinding
    private val stringProvider = StringProvider(this)

    private val viewModel: EditPersonaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPersonaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.handleEvent(EditPersonaEvent.LoadPersona(intent.extras?.getString(Constantes.EMAIL)!!))

        with(binding) {
            containedButtonEditPerson.setOnClickListener {
                viewModel.handleEvent(
                    EditPersonaEvent.EditPersona(
                        intent.getStringExtra(Constantes.EMAIL)!!,
                        binding.textFieldNombre.editText?.text.toString(),
                        binding.textFieldTelefono.editText?.text.toString(),
                        binding.textFieldFNacimiento.editText?.text.toString(),
                    )
                )
            }

            imageView.loadUrl(Constantes.IMAGE_PERSONAL)

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
                    textFieldFNacimiento.editText?.setText(formattedDate)
                }
                datePicker.show(supportFragmentManager, Constantes.DATE_PICKER_TAG)
            }
        }

        viewModel.uiState.observe(this) { uiState ->
            uiState.mensaje?.let { mensaje ->
                Timber.i(mensaje)
                Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_LONG).show()
                clearTextFieldErrors()
                loadTextFieldErrors(mensaje)
                viewModel.handleEvent(EditPersonaEvent.ClearState)
            }
            uiState.personaMostrar.let {
                with(binding) {
                    textFieldEmail.editText?.setText(it.email)
                    textFieldNombre.editText?.setText(it.nombre)
                    val formatter = DateTimeFormatter.ofPattern(Constantes.DATE_FORMAT)
                    textFieldFNacimiento.editText?.setText(it.fnacimiento?.format(formatter))
                    textFieldTelefono.editText?.setText(it.telefono)
                }
            }
            uiState.personaSinEditar?.let { persona ->
                Timber.i(Constantes.PERSONA_EDITED, persona.email)
                Snackbar.make(
                    binding.root,
                    stringProvider.getString(R.string.persona_editada),
                    Snackbar.LENGTH_LONG
                ).setAction(stringProvider.getString(R.string.snackbar_undo)) {
                    viewModel.handleEvent(EditPersonaEvent.UndoEditPersona(persona))
                }.show()
                viewModel.handleEvent(EditPersonaEvent.ClearState)
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
            stringProvider.getString(R.string.email_incorrecto) -> {
                binding.textFieldEmail.error = error
            }
            stringProvider.getString(R.string.nombre_incorrecto) -> {
                binding.textFieldNombre.error = error
            }
            stringProvider.getString(R.string.telefono_incorrecto) -> {
                binding.textFieldTelefono.error = error
            }
            stringProvider.getString(R.string.fecha_incorrecta) -> {
                binding.textFieldFNacimiento.error = error
            }
        }
    }

    private fun clearTextFieldErrors() {
        binding.textFieldEmail.error = null
        binding.textFieldNombre.error = null
        binding.textFieldTelefono.error = null
        binding.textFieldFNacimiento.error = null
    }
}
