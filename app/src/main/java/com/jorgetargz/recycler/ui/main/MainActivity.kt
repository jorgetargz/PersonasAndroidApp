package com.jorgetargz.recycler.ui.main

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
import com.jorgetargz.recycler.databinding.ActivityMainBinding
import com.jorgetargz.recycler.ui.add_hotel.AddHotelActivity
import com.jorgetargz.recycler.ui.add_persona.AddPersonaActivity
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.ui.common.loadUrl
import com.jorgetargz.recycler.ui.listado_hoteles.ListHotelesActivity
import com.jorgetargz.recycler.ui.listado_personas.ListPersonaActivity
import com.jorgetargz.recycler.util.StringProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var temp: Int = 0
    private lateinit var binding: ActivityMainBinding
    private val stringProvider = StringProvider(this)


    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        with(binding) {
            setContentView(root)

            imageViewPersonas.loadUrl(Constantes.IMAGE_PERSONAL)

            imageViewHoteles.loadUrl(Constantes.IMAGE_SKYSCRAPERS)

            containedButtonAddPerson.setOnClickListener {
                temp++
                val intent = Intent(this@MainActivity, AddPersonaActivity::class.java)
                startActivity(intent)
            }

            containedButtonAddHotel.setOnClickListener {
                temp++
                val intent = Intent(this@MainActivity, AddHotelActivity::class.java)
                startActivity(intent)
            }

            containedButtonOpenListPersonas.setOnClickListener {
                temp++
                val intent = Intent(this@MainActivity, ListPersonaActivity::class.java)
                startActivity(intent)
            }

            containedButtonOpenListHoteles.setOnClickListener {
                temp++
                val intent = Intent(this@MainActivity, ListHotelesActivity::class.java)
                startActivity(intent)
            }

        }

        viewModel.uiState.observe(this) { state ->
            state.mensaje?.let { mensaje ->
                Timber.i(mensaje)
                Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT).show()
                viewModel.handleEvent(MainEvent.ClearState)
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

