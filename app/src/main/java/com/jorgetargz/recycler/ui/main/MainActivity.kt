package com.jorgetargz.recycler.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.data.AppDatabase
import com.jorgetargz.recycler.data.RepositorioPersonas
import com.jorgetargz.recycler.databinding.ActivityMainBinding
import com.jorgetargz.recycler.domain.modelo.Persona
import com.jorgetargz.recycler.domain.usecases.personas.AddPersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.ValidarPersonaUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.ui.common.loadUrl
import com.jorgetargz.recycler.ui.listado.ListActivity
import com.jorgetargz.recycler.util.StringProvider
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {

    private var temp: Int = 0
    private lateinit var binding: ActivityMainBinding
    private val stringProvider = StringProvider.instance(this)


    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            StringProvider.instance(this),
            ValidarPersonaUseCase(),
            AddPersonaUseCase(RepositorioPersonas(AppDatabase.getDatabase(this).personasDao())),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        with(binding) {
            setContentView(root)

            imageView.loadUrl(Constantes.IMAGE_PERSONAL)

            containedButtonAddPerson.setOnClickListener {
                viewModel.handleEvent(
                    MainEvent.AddPersona(
                        textFieldEmail.editText?.text.toString(),
                        textFieldNombre.editText?.text.toString(),
                        textFieldTelefono.editText?.text.toString(),
                        textFieldFNacimiento.editText?.text.toString(),
                    )
                )
            }

            containedButtonClean.setOnClickListener {
                viewModel.handleEvent(MainEvent.CleanInputFields)
            }

            containedButtonOpenDB.setOnClickListener {
                temp++
                val intent = Intent(this@MainActivity, ListActivity::class.java)
                startActivity(intent)
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
                    textFieldFNacimiento.editText?.setText(formattedDate)
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
                viewModel.handleEvent(MainEvent.ClearState)

            }
            if (state.onAdd) {
                showDialogConfirmAdd()
                viewModel.handleEvent(MainEvent.ClearState)
            }
            if (state.cleanFields) {
                clearTextFieldErrors()
                clearTextFields()
                viewModel.handleEvent(MainEvent.ClearState)
            }
        }
    }

    private fun showDialogConfirmAdd() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(stringProvider.getString(R.string.dialog_add_title))
            .setMessage(stringProvider.getString(R.string.dialog_add_content))
            .setNegativeButton(stringProvider.getString(R.string.dialog_cancel)) { view, _ ->
                view.dismiss()
            }
            .setPositiveButton(stringProvider.getString(R.string.dialog_coonfirm)) { view, _ ->
                viewModel.handleEvent(
                    MainEvent.ConfirmAddPersona(
                        Persona(
                            binding.textFieldEmail.editText?.text.toString(),
                            binding.textFieldNombre.editText?.text.toString(),
                            LocalDate.parse(
                                binding.textFieldFNacimiento.editText?.text.toString(),
                                DateTimeFormatter.ofPattern(Constantes.DATE_FORMAT)
                            ),
                            binding.textFieldTelefono.editText?.text.toString(),
                        )
                    )
                )
                Snackbar.make(
                    binding.root,
                    stringProvider.getString(R.string.persona_añadida),
                    Snackbar.LENGTH_SHORT
                ).show()
                view.dismiss()
            }
            .setCancelable(false)
            .create()
        dialog.show()
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

    private fun clearTextFields() {
        binding.textFieldEmail.editText?.setText(Constantes.EMPTY_STRING)
        binding.textFieldNombre.editText?.setText(Constantes.EMPTY_STRING)
        binding.textFieldFNacimiento.editText?.setText(Constantes.EMPTY_STRING)
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

