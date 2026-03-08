package com.example.techaudit
//DetailActivity.kt

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

        laboratorio =
            intent.getSerializableExtra("EXTRA_LABORATORIO") as LaboratoriosEntity

        title = "Equipos - ${laboratorio.name}"

        setupRecycler()

        binding.fabAgregarEquipo.setOnClickListener {

            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("EXTRA_LABORATORIO_ID", laboratorio.id)
            startActivity(intent)
        }

        cargarEquipos()
    }

    private fun setupRecycler() {

        adapter = AuditAdapter(mutableListOf()) {}

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
}