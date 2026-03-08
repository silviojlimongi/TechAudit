package com.example.techaudit.model
//LaboratioEntity.kt

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.UUID

@Entity(tableName = "laboratorios")
data class LaboratoriosEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val location: String
) : Serializable
