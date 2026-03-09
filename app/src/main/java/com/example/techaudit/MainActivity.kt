package com.example.techaudit

//MainActivity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.adapter.LaboratorioAdapter
import com.example.techaudit.databinding.ActivityMainBinding
import com.example.techaudit.network.EquipoDto
import com.example.techaudit.network.LaboratorioDto
import com.example.techaudit.network.RetrofitClient
import com.example.techaudit.ui.LaboratorioViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: LaboratorioAdapter

    // private lateinit var database: AuditDatabase
    private val viewModel: LaboratorioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //database = (application as TechAuditApp).database
        setupRecyclerView()
        //cargarDatosdeBaseDeDatos()
        configurarDeslizarParaBorrar()

        viewModel.allLaboratorios.observe(this) { listaActualizada ->
            adapter.actualizarLista(listaActualizada)
        }

        binding.fabGestionarLaboratorios.setOnClickListener {
            val intent = Intent(this@MainActivity, AddLaboratorioActivity::class.java)
            startActivity(intent)
        }

        binding.btnSincronizar.setOnClickListener {
            if (!hayConexionInternet()) {
                Toast.makeText(
                    this,
                    "No hay conexión a internet. Verifica tu red e intenta nuevamente.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                sincronizarDatos()
            }
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
        adapter = LaboratorioAdapter(mutableListOf()) { laboratorioSeleccionado ->
            //este lambda se va a ejecutar cuando doy click a la tarjeta

            //  Toast.makeText(this, "Click en ${itemSeleccionado.nombre}", Toast.LENGTH_SHORT).show()
            //Aqui debe navegar a la pantalla de detalle

            val intent = Intent(this, DetailActivity::class.java) // conectar con la informacion de la pantalla
            //intent.putExtra("EXTRA_ITEM", itemSeleccionado)
            intent.putExtra("EXTRA_LABORATORIO", laboratorioSeleccionado)
            startActivity(intent)
        }

        binding.rvLaboratorios.adapter = adapter // conectar los datos con la interfaz, se conecta con el xml y el adapter
        binding.rvLaboratorios.layoutManager = LinearLayoutManager(this) // como se van a mostrar los datos, los ordena en una lista
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
                val laboratorioABorrar = adapter.obtenerItem(posicion)

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

                viewModel.eliminarSiNoTieneEquipos(laboratorioABorrar) { eliminado ->
                    runOnUiThread {
                        if (eliminado) {
                            Toast.makeText(this@MainActivity, "Laboratorio Eliminado", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "No se puede eliminar: el laboratorio tiene equipos asociados",
                                Toast.LENGTH_LONG
                            ).show()
                            adapter.notifyItemChanged(posicion)
                        }
                    }
                }
            }
        }

        // Conectamos este comportamiento a nuestra lista
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvLaboratorios)
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
        //cargarDatosdeBaseDeDatos()
    }

     */

    private fun sincronizarDatos() {
        val database = (application as TechAuditApp).database

        lifecycleScope.launch {
            if (!hayConexionInternet()) {
                Toast.makeText(
                    this@MainActivity,
                    "No hay conexión a internet. No se pudo sincronizar.",
                    Toast.LENGTH_LONG
                ).show()
                return@launch
            }

            try {
                binding.progressBarSync.visibility = View.VISIBLE
                binding.tvPorcentajeSync.visibility = View.VISIBLE
                binding.btnSincronizar.isEnabled = false

                binding.progressBarSync.progress = 0
                binding.tvPorcentajeSync.text = "Sincronizando... 0%"

                val laboratorios = database.laboratorioDao().getAllLaboratorios().first()
                val equipos = database.auditDao().getAllItems().first()

                val totalRegistros = laboratorios.size + equipos.size
                var enviados = 0

                if (totalRegistros == 0) {
                    Toast.makeText(
                        this@MainActivity,
                        "No existen datos locales en Room para sincronizar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                for (lab in laboratorios) {
                    val dto = LaboratorioDto(
                        id = lab.id,
                        name = lab.name,
                        location = lab.location
                    )

                    val response = RetrofitClient.api.crearLaboratorio(dto)

                    if (!response.isSuccessful) {
                        throw Exception("Error al subir laboratorio: ${lab.name}")
                    }

                    enviados++
                    val porcentaje = (enviados * 100) / totalRegistros
                    binding.progressBarSync.progress = porcentaje
                    binding.tvPorcentajeSync.text = "Sincronizando... $porcentaje%"
                }

                for (equipo in equipos) {
                    val dto = EquipoDto(
                        id = equipo.id,
                        nombre = equipo.nombre,
                        ubicacion = equipo.ubicacion,
                        fechaRegistro = equipo.fechaRegistro,
                        estado = equipo.estado.name,
                        notas = equipo.notas,
                        laboratorioId = equipo.laboratorioId
                    )

                    val response = RetrofitClient.api.crearEquipo(dto)

                    if (!response.isSuccessful) {
                        throw Exception("Error al subir equipo: ${equipo.nombre}")
                    }

                    enviados++
                    val porcentaje = (enviados * 100) / totalRegistros
                    binding.progressBarSync.progress = porcentaje
                    binding.tvPorcentajeSync.text = "Sincronizando... $porcentaje%"
                }

                Toast.makeText(
                    this@MainActivity,
                    "Sincronización completada con éxito.",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: IOException) {
                Toast.makeText(
                    this@MainActivity,
                    "Error de red. Verifica tu conexión a internet.",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: HttpException) {
                Toast.makeText(
                    this@MainActivity,
                    "Error del servidor: ${e.message()}",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "No se pudo sincronizar: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                binding.progressBarSync.visibility = View.GONE
                binding.tvPorcentajeSync.visibility = View.GONE
                binding.btnSincronizar.isEnabled = true
            }
        }
    }

    private fun hayConexionInternet(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}