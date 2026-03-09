package com.example.techaudit
//DetailActivity.kt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.adapter.AuditAdapter
import com.example.techaudit.data.AuditDatabase
import com.example.techaudit.databinding.ActivityDetailBinding
import com.example.techaudit.model.LaboratoriosEntity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var adapter: AuditAdapter
    private lateinit var laboratorio: LaboratoriosEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            binding.fabAgregarEquipo.translationY = -systemBars.bottom.toFloat()

            insets
        }
        laboratorio =
            intent.getSerializableExtra("EXTRA_LABORATORIO") as LaboratoriosEntity

        title = "Equipos - ${laboratorio.name}"

        setupRecycler()

        binding.fabAgregarEquipo.setOnClickListener {

            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("EXTRA_LABORATORIO_ID", laboratorio.id)
            //intent.putExtra("EXTRA_LABORATORIO_NOMBRE", laboratorio.name)
            startActivity(intent)
        }

        cargarEquipos()
        configurarDeslizarParaBorrar()

    }
    override fun onResume() {
        super.onResume()
        cargarEquipos()
    }

    private fun setupRecycler() {

        adapter = AuditAdapter(mutableListOf()) { itemSeleccionado ->

            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("EXTRA_ITEM_EDITAR", itemSeleccionado)
            intent.putExtra("EXTRA_LABORATORIO_ID", laboratorio.id)
            startActivity(intent)
        }

        binding.rvEquipos.adapter = adapter
        binding.rvEquipos.layoutManager = LinearLayoutManager(this)
    }
    private fun cargarEquipos() {

        val database = (application as TechAuditApp).database

        MainScope().launch {

            val equipos =
                database.auditDao().getEquiposPorLaboratorio(laboratorio.id)

            adapter.actualizarLista(equipos)
        }
    }

    private fun configurarDeslizarParaBorrar() {
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
                val itemABorrar = adapter.obtenerItem(posicion)

                val database = (application as TechAuditApp).database

                lifecycleScope.launch {
                    database.auditDao().deleteItem(itemABorrar)
                    Toast.makeText(this@DetailActivity, "Equipo eliminado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvEquipos)
    }
}