package com.example.techaudit.data
//AuditDao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.example.techaudit.model.AuditItem

@Dao
interface AuditDao {
    //Traer todos los equipos ordenados por fecha

    @Query("SELECT * FROM equipos ORDER BY fechaRegistro DESC")
    suspend fun getAllItems(): List<AuditItem> // query para traer todos los equipos

    // buscar uno solo por ID
    @Query("SELECT * FROM equipos WHERE id = :id")
    suspend fun getItemById(id: String): AuditItem?

    // insertar un nuevo equipo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: AuditItem)



    @Update
    suspend fun updateItem(item: AuditItem)


    @Delete
    suspend fun deleteItem(item: AuditItem)

    @Query("DELETE FROM equipos")
    suspend fun deleteAllItems(): Int

}