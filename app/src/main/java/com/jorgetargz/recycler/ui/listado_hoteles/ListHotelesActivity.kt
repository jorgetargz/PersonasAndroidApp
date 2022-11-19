package com.jorgetargz.recycler.ui.listado_hoteles

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
import com.jorgetargz.recycler.ui.edit_hotel.EditHotelActivity
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ListHotelesActivity : AppCompatActivity() {

    private lateinit var rvHoteles: RecyclerView
    private val stringProvider = StringProvider(this)

    private val viewModel: ListHotelesViewModel by viewModels()

    inner class ListHotelesActionsImpl : ListHotelesActions {
        override fun editHotel(cif: String) {
            val intent = Intent(this@ListHotelesActivity, EditHotelActivity::class.java)
            intent.putExtra(Constantes.CIF, cif)
            startActivity(intent)
        }

        override fun deleteHotel(cif: String) {
            viewModel.handleEvent(ListHotelesEvent.DeleteHotel(cif))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_hoteles)

        rvHoteles = this.findViewById(R.id.rvHoteles)
        val adapter = HotelesAdapter(ListHotelesActionsImpl())

        rvHoteles.adapter = adapter
        rvHoteles.layoutManager = GridLayoutManager(this, 1)

        viewModel.handleEvent(ListHotelesEvent.LoadHoteles)
        viewModel.uiState.observe(this) { state ->
            state.mensaje?.let { mensaje ->
                Timber.i(mensaje)
                Snackbar.make(rvHoteles, mensaje, Snackbar.LENGTH_SHORT).show()
                viewModel.handleEvent(ListHotelesEvent.ClearState)
            }
            state.lista?.let { listaPersonas ->
                adapter.submitList(listaPersonas)
            }
            state.hotelDeleted?.let { persona ->
                Timber.i(Constantes.HOTEL_DELETED, persona.cif)
                Snackbar.make(
                    rvHoteles,
                    stringProvider.getString(R.string.hotel_borrado),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(stringProvider.getString(R.string.snackbar_undo)) {
                        viewModel.handleEvent(ListHotelesEvent.UndoDeleteHotel(persona))
                    }
                    .show()
                viewModel.handleEvent(ListHotelesEvent.ClearState)
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        viewModel.handleEvent(ListHotelesEvent.LoadHoteles)
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
