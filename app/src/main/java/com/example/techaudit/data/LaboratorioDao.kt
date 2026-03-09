package com.example.techaudit.data

//LaboratorioDao.kt

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.techaudit.model.LaboratoriosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LaboratorioDao {

    @Query("SELECT * FROM laboratorios ORDER BY name ASC")
    fun getAllLaboratorios(): Flow<List<LaboratoriosEntity>>

    @Query("SELECT * FROM laboratorios WHERE id = :id LIMIT 1")
    suspend fun getLaboratorioById(id: String): LaboratoriosEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaboratorio(laboratorio: LaboratoriosEntity)

    @Update
    suspend fun updateLaboratorio(laboratorio: LaboratoriosEntity)

    @Delete
    suspend fun deleteLaboratorio(laboratorio: LaboratoriosEntity)

    @Query("SELECT * FROM laboratorios ORDER BY name ASC")
    fun getAllLaboratoriosLive(): LiveData<List<LaboratoriosEntity>>



}