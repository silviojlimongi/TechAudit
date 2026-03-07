package com.example.techaudit

//MainActivity

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
import com.example.techaudit.databinding.ActivityMainBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus
import java.util.UUID
import com.example.techaudit.data.AuditDao
import kotlinx.coroutines.launch
import java.util.Date
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.techaudit.ui.AuditViewModel



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AuditAdapter

    // private lateinit var database: AuditDatabase
    private val viewModel: AuditViewModel by viewModels ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //database = (application as TechAuditApp).database
        setupRecyclerView()
        //cargarDatosdeBaseDeDatos()
        configurarDeslizarParaBorrar()


        viewModel.allItems.observe(this) { listaActualizada ->
        adapter.actualizarLista(listaActualizada)
        }



        binding.fabAgregar.setOnClickListener {
            //insertarRegistro()
            val intent = Intent(this, AddEditActivity::class.java)
            startActivity(intent)
        }

        /*
        val datosMock = listOf(
            AuditItem(id = UUID.randomUUID().toString(), "Laptop HP", "Lab 1 Puesto 3", "2026-01-20", AuditStatus.PENDIENTE) ,
            AuditItem(id = UUID.randomUUID().toString(), "Laptop DELL", "Lab 1 Puesto 4", "2026-02-21", AuditStatus.OPERATIVO),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop ASUS", "Lab 1 Puesto 5", "2026-03-24", AuditStatus.DANIADO),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop LENOVO", "Lab 1 Puesto 6", "2026-01-26", AuditStatus.PENDIENTE),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop HP", "Lab 1 Puesto 3", "2026-01-20", AuditStatus.PENDIENTE) ,
            AuditItem(id = UUID.randomUUID().toString(), "Laptop DELL", "Lab 1 Puesto 4", "2026-02-21", AuditStatus.OPERATIVO),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop ASUS", "Lab 1 Puesto 5", "2026-03-24", AuditStatus.DANIADO),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop LENOVO", "Lab 1 Puesto 6", "2026-01-26", AuditStatus.PENDIENTE),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop HP", "Lab 1 Puesto 3", "2026-01-20", AuditStatus.PENDIENTE) ,
            AuditItem(id = UUID.randomUUID().toString(), "Laptop DELL", "Lab 1 Puesto 4", "2026-02-21", AuditStatus.OPERATIVO),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop ASUS", "Lab 1 Puesto 5", "2026-03-24", AuditStatus.DANIADO),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop LENOVO", "Lab 1 Puesto 6", "2026-01-26", AuditStatus.PENDIENTE),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop HP", "Lab 1 Puesto 3", "2026-01-20", AuditStatus.PENDIENTE) ,
            AuditItem(id = UUID.randomUUID().toString(), "Laptop DELL", "Lab 1 Puesto 4", "2026-02-21", AuditStatus.OPERATIVO),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop ASUS", "Lab 1 Puesto 5", "2026-03-24", AuditStatus.DANIADO),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop LENOVO", "Lab 1 Puesto 6", "2026-01-26", AuditStatus.PENDIENTE),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop HP", "Lab 1 Puesto 3", "2026-01-20", AuditStatus.PENDIENTE) ,
            AuditItem(id = UUID.randomUUID().toString(), "Laptop DELL", "Lab 1 Puesto 4", "2026-02-21", AuditStatus.OPERATIVO),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop ASUS", "Lab 1 Puesto 5", "2026-03-24", AuditStatus.DANIADO),
            AuditItem(id = UUID.randomUUID().toString(), "Laptop LENOVO", "Lab 1 Puesto 6", "2026-01-26", AuditStatus.PENDIENTE),

            AuditItem(id = UUID.randomUUID().toString(), "Laptop ACER", "Lab 1 Puesto 7", "2026-01-29", AuditStatus.NO_ENCONTRADO)

        )
        */


       // enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }



    //private fun setupRecyclerView(lista: List<AuditItem>) {
    private fun setupRecyclerView() {

        //Inicializar el Adapter pansando la lista y la accion del click
        adapter = AuditAdapter(mutableListOf()) { itemSeleccionado ->
            //este lambda se va a ejecutar cuando doy click a la tarjeta

            //  Toast.makeText(this, "Click en ${itemSeleccionado.nombre}", Toast.LENGTH_SHORT).show()
            //Aqui debe navegar a la pantalla de detalle

            val intent = Intent(this, AddEditActivity::class.java) // conectar con la informacion de la pantalla
            //intent.putExtra("EXTRA_ITEM", itemSeleccionado)
            intent.putExtra("EXTRA_ITEM_EDITAR", itemSeleccionado)
            startActivity(intent)


        }

        binding.rvAuditoria.adapter = adapter // conectar los datos con la interfaz, se conecta con el xml y el adapter
        binding.rvAuditoria.layoutManager = LinearLayoutManager(this) // como se van a mostrar los datos, los ordena en una lista

    }

    private fun configurarDeslizarParaBorrar() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0, // No nos importa mover arriba/abajo
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // Permitir deslizar a izq y der
        ) {
            override fun onMove(r: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder): Boolean = false

            // Este método se dispara cuando el usuario suelta el dedo tras deslizar
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val itemABorrar = adapter.obtenerItem(posicion)

               /*
                lifecycleScope.launch {
                    // 1. Borrar de la Base de Datos
                    database.auditDao().delete(itemABorrar)

                    // 2. Borrar de la pantalla (Animación fluida)
                    adapter.listaAuditoria.removeAt(posicion)
                    adapter.notifyItemRemoved(posicion)

                    Toast.makeText(this@MainActivity, "Equipo Eliminado", Toast.LENGTH_SHORT).show()
                }

                */
                viewModel.delete(itemABorrar)
                Toast.makeText(this@MainActivity, "Equipo Eliminado", Toast.LENGTH_SHORT).show()

            }
        }

        // Conectamos este comportamiento a nuestra lista
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvAuditoria)
    }

    /*
    private fun cargarDatosdeBaseDeDatos() {
        lifecycleScope.launch() {
            val datos = database.auditDao().getAllItems()
            if (datos.isEmpty()) {
                Toast.makeText(this@MainActivity, "No exiente datos", Toast.LENGTH_SHORT).show()
            } else {
                adapter.actualizarlista(datos)
            }
        }
    }
    /*
        //no se utiliza por el momento
        private fun insertarRegistro(){
            val nuevoItem = AuditItem(
                id= UUID.randomUUID().toString(),
                nombre = "Nueva Equipo #${(1..100).random()}",
                ubicacion = "Recepcion",
                fechaRegistro = Date().toString(),
                estado=AuditStatus.PENDIENTE,
                notas= "Registro aleatorio"
            )
            lifecycleScope.launch(){
                database.auditDao().insertItem(nuevoItem)

                Toast.makeText(this@MainActivity, "Registro insertado", Toast.LENGTH_SHORT).show()
                cargarDatosdeBaseDeDatos()
            }

        }

     */

    override fun onResume() {
        super.onResume()
        cargarDatosdeBaseDeDatos()
    }

     */
}




