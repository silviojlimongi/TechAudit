package com.example.techaudit.adapter
//LaboratorioAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit.databinding.ItemLaboratorioBinding
import com.example.techaudit.model.LaboratoriosEntity

class LaboratorioAdapter(
    private val listaLaboratorios: MutableList<LaboratoriosEntity>,
    private val onItemSelected: (LaboratoriosEntity) -> Unit
) : RecyclerView.Adapter<LaboratorioAdapter.LaboratorioViewHolder>() {

    inner class LaboratorioViewHolder(val binding: ItemLaboratorioBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaboratorioViewHolder {
        val binding = ItemLaboratorioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LaboratorioViewHolder(binding)
    }

    override fun getItemCount(): Int = listaLaboratorios.size

    fun actualizarLista(nuevaLista: List<LaboratoriosEntity>) {
        listaLaboratorios.clear()
        listaLaboratorios.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    fun obtenerItem(posicion: Int): LaboratoriosEntity {
        return listaLaboratorios[posicion]
    }

    override fun onBindViewHolder(holder: LaboratorioViewHolder, position: Int) {
        val laboratorio = listaLaboratorios[position]

        holder.binding.tvNombreLaboratorio.text = laboratorio.name
        holder.binding.tvEdificioLaboratorio.text = laboratorio.location

        holder.itemView.setOnClickListener {
            onItemSelected(laboratorio)
        }
    }
}