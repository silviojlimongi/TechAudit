package com.example.techaudit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.adapter.LaboratorioAdapter
import com.example.techaudit.databinding.ActivityLaboratoriosBinding
import com.example.techaudit.model.LaboratoriosEntity
import com.example.techaudit.ui.LaboratorioViewModel

class LaboratoriosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaboratoriosBinding
    private lateinit var adapter: LaboratorioAdapter

    private val viewModel: LaboratorioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLaboratoriosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        configurarSwipeParaEliminar()

        viewModel.allLaboratorios.observe(this) { lista ->
            adapter.actualizarLista(lista)
        }

        binding.fabAgregarLaboratorio.setOnClickListener {
            val intent = Intent(this, AddLaboratorioActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = LaboratorioAdapter(mutableListOf()) { laboratorio ->
            Toast.makeText(this, "Laboratorio: ${laboratorio.name}", Toast.LENGTH_SHORT).show()
        }

        binding.rvLaboratorios.adapter = adapter
        binding.rvLaboratorios.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarSwipeParaEliminar() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val laboratorio = adapter.obtenerItem(posicion)

                viewModel.eliminarSiNoTieneEquipos(laboratorio) { eliminado ->
                    runOnUiThread {
                        if (eliminado) {
                            Toast.makeText(
                                this@LaboratoriosActivity,
                                "Laboratorio eliminado",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@LaboratoriosActivity,
                                "No se puede eliminar: tiene equipos asociados",
                                Toast.LENGTH_LONG
                            ).show()
                            adapter.notifyItemChanged(posicion)
                        }
                    }
                }
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvLaboratorios)
    }
}