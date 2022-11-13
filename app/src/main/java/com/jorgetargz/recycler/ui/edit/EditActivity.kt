package com.jorgetargz.recycler.ui.edit

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.data.AppDatabase
import com.jorgetargz.recycler.data.RepositorioPersonas
import com.jorgetargz.recycler.databinding.ActivityEditBinding
import com.jorgetargz.recycler.domain.usecases.personas.GetPersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.UpdatePersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.ValidarPersonaUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.ui.common.loadUrl
import com.jorgetargz.recycler.util.StringProvider
import timber.log.Timber
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private val stringProvider = StringProvider.instance(this)

    private val viewModel: EditViewModel by viewModels {
        EditViewModelFactory(
            StringProvider.instance(this),
            ValidarPersonaUseCase(),
            GetPersonaUseCase(RepositorioPersonas(AppDatabase.getDatabase(this).personasDao())),
            UpdatePersonaUseCase(RepositorioPersonas(AppDatabase.getDatabase(this).personasDao())),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.handleEvent(EditEvent.LoadPersona(intent.extras?.getString(Constantes.EMAIL)!!))

        with(binding) {
            containedButtonEditPerson.setOnClickListener {
                viewModel.handleEvent(
                    EditEvent.EditPersona(
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
                viewModel.handleEvent(EditEvent.ClearState)
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
                    viewModel.handleEvent(EditEvent.UndoEditPersona(persona))
                }.show()
                viewModel.handleEvent(EditEvent.ClearState)
            }
        }
    }

    private fun loadTextFieldErrors(error: String) {
        val stringProvider = StringProvider.instance(this)
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
