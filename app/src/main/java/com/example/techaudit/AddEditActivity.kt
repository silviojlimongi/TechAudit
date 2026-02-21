package com.example.techaudit
//AddEditActivity
import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.techaudit.databinding.ActivityAddEditBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
//import com.example.techaudit.data.AuditDao


class AddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)



        enableEdgeToEdge()


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //configurar el spiner
        setupSpinner()

        binding.btnGuardar.setOnClickListener {
            guardarRegistro()
        }
    }


    private fun setupSpinner() {
        // Truco: Convertimos el Enum a una lista de Strings para el Spinner
        val estados = AuditStatus.values() // [PENDIENTE, OPERATIVO, ...]

        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            estados
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spEstado.adapter = adapter
    }

    private fun guardarRegistro() {
        // A. Capturar textos
        val nombre = binding.etNombre.text.toString()
        val ubicacion = binding.etUbicacion.text.toString()
        val notas = binding.etNotas.text.toString()

        // B. Validar (Regla de Negocio)
        if (nombre.isBlank() || ubicacion.isBlank()) {
            Toast.makeText(this, "Nombre y Ubicación son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // C. Obtener el Estado seleccionado del Spinner
        // El Spinner nos da la posición (0, 1, 2...), la usamos para buscar en el Enum
        val estadoSeleccionado = binding.spEstado.selectedItem as AuditStatus

        // D. Crear el objeto
        val nuevoItem = AuditItem(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            ubicacion = ubicacion,
            fechaRegistro = Date().toString(), // Fecha de hoy
            estado = estadoSeleccionado,
            notas = notas
        )

        // E. Guardar en BD (Corutina)
        val database = (application as TechAuditApp).database

        lifecycleScope.launch() {
            database.auditDao().insertItem(nuevoItem)

            // F. Cerrar y volver
            Toast.makeText(this@AddEditActivity, "Guardado!", Toast.LENGTH_SHORT).show()

            finish() // Esto cierra la actividad y nos regresa al Main
        }
    }
}