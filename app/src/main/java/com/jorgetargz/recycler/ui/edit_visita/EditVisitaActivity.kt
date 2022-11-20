package com.jorgetargz.recycler.ui.edit_visita

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
import com.jorgetargz.recycler.databinding.ActivityEditVisitaBinding
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.ui.common.loadUrl
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EditVisitaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditVisitaBinding
    private val stringProvider = StringProvider(this)

    private val viewModel: EditVisitaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditVisitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cifHotel = intent.extras?.getString(Constantes.CIF)
        val emailPersona = intent.extras?.getString(Constantes.EMAIL)

        viewModel.handleEvent(EditVisitaEvent.LoadVisita(cifHotel!!, emailPersona!!))

        with(binding) {
            containedButtonEditVisita.setOnClickListener {
                viewModel.handleEvent(
                    EditVisitaEvent.EditVisita(
                        cifHotel,
                        emailPersona,
                        binding.textFieldValoracion.editText?.text.toString(),
                    )
                )
            }

            imageViewReception.loadUrl(Constantes.IMAGE_RECEPTION)

        }

        viewModel.uiState.observe(this) { uiState ->
            uiState.mensaje?.let { mensaje ->
                Timber.i(mensaje)
                Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_LONG).show()
                viewModel.handleEvent(EditVisitaEvent.ClearState)
            }
            uiState.visitaMostrar.let {
                with(binding) {
                    textFieldCIF.editText?.setText(it.cifHotel)
                    textFieldEmail.editText?.setText(it.emailPersona)
                }
            }
            uiState.visitaSinEditar?.let { visita ->
                Timber.i(Constantes.HOTEL_EDITED, visita.cifHotel, visita.emailPersona)
                Snackbar.make(
                    binding.root,
                    stringProvider.getString(R.string.visita_actualizada),
                    Snackbar.LENGTH_LONG
                ).setAction(stringProvider.getString(R.string.snackbar_undo)) {
                    viewModel.handleEvent(EditVisitaEvent.UndoEditVisita(visita))
                }.show()
                viewModel.handleEvent(EditVisitaEvent.ClearState)
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
}
