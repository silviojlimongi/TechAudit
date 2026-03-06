package com.example.techaudit.data
//AuditDao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.example.techaudit.model.AuditItem
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditDao {
    //Traer todos los equipos ordenados por fecha

    @Query("SELECT * FROM equipos ORDER BY fechaRegistro DESC")
    //suspend fun getAllItems(): List<AuditItem> // query para traer todos los equipos
    fun getAllItems(): Flow<List<AuditItem>>


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

    //borrar util para pruebas
    //@Query("DELETE FROM equipos")
    //suspend fun deleteAllItems(): Int
    @Delete
    suspend fun delete(item: AuditItem)


}