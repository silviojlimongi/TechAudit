package com.example.techaudit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.techaudit.databinding.ActivityAddLaboratorioBinding
import com.example.techaudit.model.LaboratoriosEntity
import com.example.techaudit.ui.LaboratorioViewModel

class AddLaboratorioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLaboratorioBinding
    private val viewModel: LaboratorioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddLaboratorioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGuardarLaboratorio.setOnClickListener {
            guardarLaboratorio()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun guardarLaboratorio() {
        val nombre = binding.etNombreLaboratorio.text.toString().trim()
        val edificio = binding.etEdificioLaboratorio.text.toString().trim()

        if (nombre.isBlank() || edificio.isBlank()) {
            Toast.makeText(this, "Nombre y edificio son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val laboratorio = LaboratoriosEntity(
            name = nombre,
            location = edificio
        )

        viewModel.insert(laboratorio)
        Toast.makeText(this, "Laboratorio guardado", Toast.LENGTH_SHORT).show()
        finish()
    }
}