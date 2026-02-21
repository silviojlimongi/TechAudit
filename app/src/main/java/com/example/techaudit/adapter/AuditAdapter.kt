package com.example.techaudit.adapter
//AuditAdapter
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.databinding.ItemAuditBinding
import com.example.techaudit.model.AuditItem
import com.example.techaudit.model.AuditStatus

class AuditAdapter(
    private val listaAuditoria: MutableList<AuditItem>, // cambiada de list a mutable
    private val onItemSelected: (AuditItem) -> Unit
) : RecyclerView.Adapter<AuditAdapter.AuditViewHolder>() {

    inner class AuditViewHolder(val binding: ItemAuditBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuditViewHolder {
        val binding = ItemAuditBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AuditViewHolder(binding)
    }

    override fun getItemCount(): Int = listaAuditoria.size


    fun actualizarlista(nuevaLista: List<AuditItem>) {
        listaAuditoria.clear()
        listaAuditoria.addAll(nuevaLista)
        notifyDataSetChanged() // refrescar la pantalla
    }





    override fun onBindViewHolder(holder: AuditViewHolder, position: Int) {
        val item = listaAuditoria[position]

        // asignar textos
        holder.binding.tvNombreEquipo.text = item.nombre
        holder.binding.tvUbicacion.text = item.ubicacion
        holder.binding.tvEstadoLabel.text = item.estado.name

        // lógica visual: cambiar colores según estado
        val colorEstado = when (item.estado) {
            AuditStatus.OPERATIVO -> Color.parseColor("#4CAF50")
            AuditStatus.PENDIENTE -> Color.parseColor("#9E9E9E")
            AuditStatus.DANIADO -> Color.parseColor("#F44336")
            AuditStatus.NO_ENCONTRADO -> Color.BLACK
        }

        // aplicar el color (elige lo que tenga sentido en tu layout)
        holder.binding.tvEstadoLabel.setTextColor(colorEstado)

        // click en todo el elemento
        holder.itemView.setOnClickListener {
            onItemSelected(item)
        }
    }
}
