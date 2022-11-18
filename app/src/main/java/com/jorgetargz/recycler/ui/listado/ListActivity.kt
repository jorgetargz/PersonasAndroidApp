package com.jorgetargz.recycler.ui.listado

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.ui.edit.EditActivity
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ListActivity : AppCompatActivity() {

    private lateinit var rvPersonas: RecyclerView
    private val stringProvider = StringProvider(this)

    private val viewModel: ListViewModel by viewModels()

    inner class ListActionsImpl : ListActions {
        override fun editPersona(email: String) {
            val intent = Intent(this@ListActivity, EditActivity::class.java)
            intent.putExtra(Constantes.EMAIL, email)
            startActivity(intent)
        }

        override fun deletePersona(email: String) {
            viewModel.handleEvent(ListEvent.DeletePersona(email))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        rvPersonas = this.findViewById(R.id.rvPersonas)
        val adapter = PersonasAdapter(ListActionsImpl())

        rvPersonas.adapter = adapter
        rvPersonas.layoutManager = GridLayoutManager(this, 1)

        viewModel.handleEvent(ListEvent.LoadPersonas)
        viewModel.uiState.observe(this) { state ->
            state.mensaje?.let { mensaje ->
                Timber.i(mensaje)
                Snackbar.make(rvPersonas, mensaje, Snackbar.LENGTH_SHORT).show()
                viewModel.handleEvent(ListEvent.ClearState)
            }
            state.lista?.let { listaPersonas ->
                adapter.submitList(listaPersonas)
            }
            state.personaDeleted?.let { persona ->
                Timber.i(Constantes.PERSONA_DELETED, persona.email)
                Snackbar.make(
                    rvPersonas,
                    stringProvider.getString(R.string.persona_borrada),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(stringProvider.getString(R.string.snackbar_undo)) {
                        viewModel.handleEvent(ListEvent.UndoDeletePersona(persona))
                    }
                    .show()
                viewModel.handleEvent(ListEvent.ClearState)
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        viewModel.handleEvent(ListEvent.LoadPersonas)
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
