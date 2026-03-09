package com.example.techaudit.data

import com.example.techaudit.model.LaboratoriosEntity
import kotlinx.coroutines.flow.Flow

class LaboratorioRepository(private val laboratorioDao: LaboratorioDao) {

    val allLaboratorios: Flow<List<LaboratoriosEntity>> = laboratorioDao.getAllLaboratorios()

    suspend fun insert(laboratorio: LaboratoriosEntity) {
        laboratorioDao.insertLaboratorio(laboratorio)
    }

    suspend fun update(laboratorio: LaboratoriosEntity) {
        laboratorioDao.updateLaboratorio(laboratorio)
    }

    suspend fun delete(laboratorio: LaboratoriosEntity) {
        laboratorioDao.deleteLaboratorio(laboratorio)
    }

    suspend fun getById(id: String): LaboratoriosEntity? {
        return laboratorioDao.getLaboratorioById(id)
    }
}