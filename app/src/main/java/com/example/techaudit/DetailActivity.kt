package com.example.techaudit

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.techaudit.databinding.ActivityDetailBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //1 recuperar el objeto enviado

        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("auditItem", AuditItem::class.java)
        }else{
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_ITEM")

        }
        // mostra los datos si existe el objeto

      item?.let{
           mostrarDetalles(it)
      }
        // 2) Mostrar los datos si existe el objeto



        ViewCompat.setOnApplyWindowInsetsListener((binding.root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun mostrarDetalles(item: AuditItem){
        binding.tvDetalleNombre.text = item.nombre
        binding.tvDetalleId.text = "ID: ${item.id.substring(0,8)}..." // mostrar 8 caracteres
        binding.tvDetalleUbicacion.text = item.ubicacion
        binding.tvDetalleFecha.text = item.fechaRegistro
        binding.tvDetalleNotas.text = item.notas.ifEmpty { "Sin observaciones." }

                //3 logica visual segun el estado (pintar la cabeza)
        val color = when (item.estado){
            AuditStatus.OPERATIVO -> Color.parseColor("#4CAF50")
            AuditStatus.PENDIENTE -> Color.parseColor("#9E9E9E")
            AuditStatus.DANIADO -> Color.parseColor("#F44336")
            AuditStatus.NO_ENCONTRADO -> Color.BLACK
        }

        binding.viewHeaderStatus.setBackgroundColor(color) // COLOR DE LA CABECERA
        title = "Detalle de ${item.nombre}" // titulo de la pantalla

    }
}
