package com.example.techaudit.data


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.techaudit.model.LaboratoriosEntity

@Dao
interface LaboratorioDao {

    @Query("SELECT * FROM laboratorios ORDER BY name ASC")
    suspend fun getAllLaboratorios(): List<LaboratoriosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(laboratorios: List<LaboratoriosEntity>)
}