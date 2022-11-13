package com.jorgetargz.recycler.ui.listado

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jorgetargz.recycler.R
import com.jorgetargz.recycler.data.AppDatabase
import com.jorgetargz.recycler.data.RepositorioPersonas
import com.jorgetargz.recycler.domain.usecases.personas.AddPersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.DeletePersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.GetPersonaUseCase
import com.jorgetargz.recycler.domain.usecases.personas.GetPersonasUseCase
import com.jorgetargz.recycler.ui.common.Constantes
import com.jorgetargz.recycler.ui.edit.EditActivity
import com.jorgetargz.recycler.util.StringProvider
import timber.log.Timber

class ListActivity : AppCompatActivity() {

    private lateinit var rvPersonas: RecyclerView
    private val stringProvider = StringProvider.instance(this)

    private val viewModel: ListViewModel by viewModels {
        ListViewModelFactory(
            StringProvider.instance(this),
            GetPersonasUseCase(RepositorioPersonas(AppDatabase.getDatabase(this).personasDao())),
            DeletePersonaUseCase(RepositorioPersonas(AppDatabase.getDatabase(this).personasDao())),
            AddPersonaUseCase(RepositorioPersonas(AppDatabase.getDatabase(this).personasDao())),
            GetPersonaUseCase(RepositorioPersonas(AppDatabase.getDatabase(this).personasDao())),
        )
    }

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
                Snackbar.make(rvPersonas, stringProvider.getString(R.string.persona_borrada), Snackbar.LENGTH_LONG)
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
}
