package com.example.techaudit

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.techaudit.data.AuditDatabase
import com.example.techaudit.databinding.ActivityAddEditBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus
import com.example.techaudit.model.LaboratoriosEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding
    private var itemEditar: AuditItem? = null
    private var laboratorioIdFijo: String? = null

    private var listaLaboratorios: List<LaboratoriosEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        laboratorioIdFijo = intent.getStringExtra("EXTRA_LABORATORIO_ID")

        setupSpinnerEstado()

        if (intent.hasExtra("EXTRA_ITEM_EDITAR")) {
            itemEditar = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("EXTRA_ITEM_EDITAR", AuditItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("EXTRA_ITEM_EDITAR")
            }
        }

        itemEditar?.let { item ->
            binding.etNombre.setText(item.nombre)
            binding.etUbicacion.setText(item.ubicacion)
            binding.etNotas.setText(item.notas)

            val posicionSpinner = AuditStatus.values().indexOf(item.estado)
            binding.spEstado.setSelection(posicionSpinner)
        }

        val database = (application as TechAuditApp).database

        lifecycleScope.launch {
            asegurarLaboratorios(database)
            listaLaboratorios = database.laboratorioDao().getAllLaboratorios().first()
            setupSpinnerLaboratorios(listaLaboratorios)

            if (itemEditar == null && !laboratorioIdFijo.isNullOrEmpty()) {
                val index = listaLaboratorios.indexOfFirst { it.id == laboratorioIdFijo }
                if (index >= 0) {
                    binding.spLaboratorio.setSelection(index)
                    binding.spLaboratorio.isEnabled = false
                    binding.spLaboratorio.alpha = 0.6f
                }
            }

            itemEditar?.let { item ->
                val index = listaLaboratorios.indexOfFirst { it.id == item.laboratorioId }
                if (index >= 0) {
                    binding.spLaboratorio.setSelection(index)
                }
            }
        }

        binding.btnGuardar.setOnClickListener {
            guardarOActualizar()
        }

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupSpinnerEstado() {
        val estados = AuditStatus.values()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            estados
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEstado.adapter = adapter
    }

    private fun setupSpinnerLaboratorios(laboratorios: List<LaboratoriosEntity>) {
        val nombres = laboratorios.map { "${it.name} - ${it.location}" }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nombres
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spLaboratorio.adapter = adapter
    }

    private suspend fun asegurarLaboratorios(database: AuditDatabase) {
        val laboratorioDao = database.laboratorioDao()
        val existentes = laboratorioDao.getAllLaboratorios().first()

        if (existentes.isEmpty()) {
            val laboratoriosBase = listOf(
                LaboratoriosEntity(name = "Lab 1", location = "Primer piso"),
                LaboratoriosEntity(name = "Lab 2", location = "Segundo piso"),
                LaboratoriosEntity(name = "Lab Redes", location = "Bloque B")
            )

            laboratoriosBase.forEach { laboratorio ->
                laboratorioDao.insertLaboratorio(laboratorio)
            }
        }
    }

    private fun guardarOActualizar() {
        val nombre = binding.etNombre.text.toString().trim()
        val ubicacion = binding.etUbicacion.text.toString().trim()
        val notas = binding.etNotas.text.toString().trim()

        if (nombre.isBlank() || ubicacion.isBlank()) {
            Toast.makeText(this, "Nombre y ubicación son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (listaLaboratorios.isEmpty()) {
            Toast.makeText(this, "No hay laboratorios disponibles", Toast.LENGTH_SHORT).show()
            return
        }

        val estadoSeleccionado = binding.spEstado.selectedItem as AuditStatus

        val laboratorioIdSeleccionado = if (itemEditar == null && !laboratorioIdFijo.isNullOrEmpty()) {
            laboratorioIdFijo!!
        } else {
            val laboratorioSeleccionado = listaLaboratorios[binding.spLaboratorio.selectedItemPosition]
            laboratorioSeleccionado.id
        }

        val database = (application as TechAuditApp).database

        lifecycleScope.launch {
            if (itemEditar == null) {
                val nuevoItem = AuditItem(
                    id = UUID.randomUUID().toString(),
                    nombre = nombre,
                    ubicacion = ubicacion,
                    fechaRegistro = Date().toString(),
                    estado = estadoSeleccionado,
                    notas = notas,
                    laboratorioId = laboratorioIdSeleccionado
                )
                database.auditDao().insertItem(nuevoItem)
                Toast.makeText(this@AddEditActivity, "Guardado", Toast.LENGTH_SHORT).show()
            } else {
                val itemActualizado = itemEditar!!.copy(
                    nombre = nombre,
                    ubicacion = ubicacion,
                    estado = estadoSeleccionado,
                    notas = notas,
                    laboratorioId = laboratorioIdSeleccionado
                )
                database.auditDao().updateItem(itemActualizado)
                Toast.makeText(this@AddEditActivity, "Actualizado", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }
}