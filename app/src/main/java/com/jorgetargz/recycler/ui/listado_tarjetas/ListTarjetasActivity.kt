package com.jorgetargz.recycler.ui.listado_tarjetas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.ui.add_tarjeta.AddTarjetaActivity
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ListTarjetasActivity : AppCompatActivity() {

    private lateinit var rvTarjetas: RecyclerView
    private val stringProvider = StringProvider(this)
    private var email: String? = null

    private val viewModel: ListTarjetasViewModel by viewModels()

    inner class ListTarjetasActionsImpl : ListTarjetasActions {
        override fun deleteTarjeta(numero: String) {
            viewModel.handleEvent(ListTarjetasEvent.DeleteTarjeta(numero))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_tarjetas)

        rvTarjetas = this.findViewById(R.id.rvTarjetas)
        val adapter = TarjetasAdapter(ListTarjetasActionsImpl())

        rvTarjetas.adapter = adapter

        email = intent.extras?.getString(Constantes.EMAIL)
        if (email != null) {
            viewModel.handleEvent(ListTarjetasEvent.LoadTarjetasByEmail(email!!))
        }

        viewModel.uiState.observe(this) { state ->
            state.mensaje?.let { mensaje ->
                Timber.i(mensaje)
                Snackbar.make(rvTarjetas, mensaje, Snackbar.LENGTH_SHORT).show()
                viewModel.handleEvent(ListTarjetasEvent.ClearState)
            }
            state.lista?.let { listaPersonas ->
                adapter.submitList(listaPersonas)
            }
            state.tarjetaDeleted?.let { tarjeta ->
                Timber.i(Constantes.TARJETA_DELETED)
                Snackbar.make(
                    rvTarjetas,
                    stringProvider.getString(R.string.tarjeta_borrada),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(stringProvider.getString(R.string.snackbar_undo)) {
                        viewModel.handleEvent(ListTarjetasEvent.UndoDeleteTarjeta(tarjeta))
                    }
                    .show()
                viewModel.handleEvent(ListTarjetasEvent.ClearState)
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        if (email != null) {
            viewModel.handleEvent(ListTarjetasEvent.LoadTarjetasByEmail(email!!))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tarjetas, menu)
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
            R.id.addTarjeta -> {
                val intent = Intent(this, AddTarjetaActivity::class.java)
                intent.putExtra(Constantes.EMAIL, email)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
