package com.jorgetargz.recycler.ui.listado_visitas

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ListVisitasActivity : AppCompatActivity() {

    private lateinit var rvVisitas: RecyclerView
    private val stringProvider = StringProvider(this)

    private val viewModel: ListVisitasViewModel by viewModels()

    inner class ListVisitasActionsImpl : ListVisitasActions {
        override fun editVisita(cif: String, email: String) {
//            val intent = Intent(this@ListVisitasActivity, EditHotelActivity::class.java)
//            intent.putExtra(Constantes.CIF, cif)
//            intent.putExtra(Constantes.EMAIL, email)
//            startActivity(intent)
        }

        override fun deleteVisita(cif: String, email: String) {
            viewModel.handleEvent(ListVisitasEvent.DeleteVisita(cif, email))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_visitas)

        rvVisitas = this.findViewById(R.id.rvVisitas)
        val adapter = VisitasAdapter(ListVisitasActionsImpl())

        rvVisitas.adapter = adapter
        rvVisitas.layoutManager = GridLayoutManager(this, 1)

        viewModel.handleEvent(ListVisitasEvent.LoadVisitas)

        viewModel.uiState.observe(this) { state ->
            state.mensaje?.let { mensaje ->
                Timber.i(mensaje)
                Snackbar.make(rvVisitas, mensaje, Snackbar.LENGTH_SHORT).show()
                viewModel.handleEvent(ListVisitasEvent.ClearState)
            }
            state.lista?.let { listaVisitas ->
                adapter.submitList(listaVisitas)
            }
            state.visitaDeleted?.let { visita ->
                Timber.i(Constantes.VISITA_DELETED, visita.cifHotel, visita.emailPersona)
                Snackbar.make(
                    rvVisitas,
                    stringProvider.getString(R.string.visita_eliminada),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(stringProvider.getString(R.string.snackbar_undo)) {
                        viewModel.handleEvent(ListVisitasEvent.UndoDeleteVisita(visita))
                    }
                    .show()
                viewModel.handleEvent(ListVisitasEvent.ClearState)
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        viewModel.handleEvent(ListVisitasEvent.LoadVisitas)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_visitas, menu)
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
            R.id.emailFilter -> {
                val txtEmail = EditText(this)
                txtEmail.hint = stringProvider.getString(R.string.helper_text_email)

                AlertDialog.Builder(this)
                    .setTitle(stringProvider.getString(R.string.filtrar_email))
                    .setView(txtEmail)
                    .setPositiveButton(
                        stringProvider.getString(R.string.filtrar)
                    ) { dialog, _ ->
                        val email = txtEmail.text.toString()
                        viewModel.handleEvent(ListVisitasEvent.LoadVisitasByPersonaEmail(email))
                        dialog.dismiss()
                    }
                    .setNegativeButton(
                        stringProvider.getString(R.string.dialog_dismiss)
                    ) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }
            R.id.cifFilter -> {
                val txtCIF = EditText(this)
                txtCIF.hint = stringProvider.getString(R.string.helper_text_CIF)

                AlertDialog.Builder(this)
                    .setTitle(stringProvider.getString(R.string.filtrar_email))
                    .setView(txtCIF)
                    .setPositiveButton(
                        stringProvider.getString(R.string.filtrar)
                    ) { dialog, _ ->
                        val cif = txtCIF.text.toString()
                        viewModel.handleEvent(ListVisitasEvent.LoadVisitasByHotelCIF(cif))
                        dialog.dismiss()
                    }
                    .setNegativeButton(
                        stringProvider.getString(R.string.dialog_dismiss)
                    ) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
