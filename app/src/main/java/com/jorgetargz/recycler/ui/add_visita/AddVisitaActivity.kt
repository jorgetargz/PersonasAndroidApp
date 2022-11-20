package com.jorgetargz.recycler.ui.add_visita

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.databinding.ActivityAddVisitaBinding
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.ui.common.loadUrl
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddVisitaActivity : AppCompatActivity() {

    private var temp: Int = 0
    private lateinit var binding: ActivityAddVisitaBinding
    private val stringProvider = StringProvider(this)


    private val viewModel: AddVisitaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVisitaBinding.inflate(layoutInflater)
        viewModel.handleEvent(AddVisitaEvent.LoadDropdownLists)

        with(binding) {
            setContentView(root)

            imageViewSkyline.loadUrl(Constantes.IMAGE_RECEPTION)

            containedButtonAddPerson.setOnClickListener {
                viewModel.handleEvent(
                    AddVisitaEvent.AddVisita(
                        textFieldCIF.editText?.text.toString(),
                        textFieldEmail.editText?.text.toString(),
                        textFieldValoracion.editText?.text.toString(),
                    )
                )
            }

            containedButtonClean.setOnClickListener {
                viewModel.handleEvent(AddVisitaEvent.CleanInputFields)
            }
        }

        viewModel.uiState.observe(this) { state ->
            state.mensaje?.let { mensaje ->
                Timber.i(mensaje)
                Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT).show()
                viewModel.handleEvent(AddVisitaEvent.ClearState)
            }
            state.visitaAdded?.let { visita ->
                Timber.i(Constantes.VISITA_ADDED)
                Snackbar.make(
                    binding.root,
                    stringProvider.getString(R.string.visita_añadida),
                    Snackbar.LENGTH_LONG
                ).setAction(stringProvider.getString(R.string.snackbar_undo)) {
                    viewModel.handleEvent(AddVisitaEvent.UndoAddVisita(visita))
                }.show()
                viewModel.handleEvent(AddVisitaEvent.ClearState)
            }
            state.cifList?.let { cifList ->
                val adapterCIFS = ArrayAdapter(this, R.layout.item_dropdown, cifList)
                (binding.textFieldCIF.editText as? AutoCompleteTextView)?.setAdapter(adapterCIFS)
            }
            state.emailList?.let { emailList ->
                val adapterEmails = ArrayAdapter(this, R.layout.item_dropdown, emailList)
                (binding.textFieldEmail.editText as? AutoCompleteTextView)?.setAdapter(adapterEmails)
            }
            if (state.cleanFields) {
                viewModel.handleEvent(AddVisitaEvent.ClearState)
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

