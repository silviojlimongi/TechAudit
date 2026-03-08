package com.example.techaudit.ui
//LaboratorioViewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.techaudit.data.AuditDatabase
import com.example.techaudit.model.LaboratoriosEntity
import kotlinx.coroutines.launch

class LaboratorioViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AuditDatabase.getDatabase(application)

    val allLaboratorios: LiveData<List<LaboratoriosEntity>> =
        database.laboratorioDao().getAllLaboratoriosLive()

    fun insert(laboratorio: LaboratoriosEntity) {
        viewModelScope.launch {
            database.laboratorioDao().insertLaboratorio(laboratorio)
        }
    }

    fun eliminarSiNoTieneEquipos(
        laboratorio: LaboratoriosEntity,
        callback: (Boolean) -> Unit
    ) {
        viewModelScope.launch {

            val cantidadEquipos =
                database.auditDao().contarEquiposPorLaboratorio(laboratorio.id)

            if (cantidadEquipos == 0) {
                database.laboratorioDao().deleteLaboratorio(laboratorio)
                callback(true)
            } else {
                callback(false)
            }
        }
    }
}