package com.example.techaudit.model

//model/AuditItem
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import androidx.room.Index

import androidx.room.PrimaryKey
import androidx.room.ForeignKey


@Entity(tableName = "equipos",
    //relaciones entre tablas equipos y laboratorios
    foreignKeys = [
        ForeignKey(
            entity = LaboratoriosEntity::class,
            parentColumns = ["id"],
            childColumns = ["laboratorioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["laboratorioId"])]
)
@Parcelize
data class AuditItem ( // para pasar los datos entre pantallas activities

    @PrimaryKey
    val id: String, // uuid o codigo de barras


    val nombre: String,
    val ubicacion: String,
    val fechaRegistro: String,
    var estado: AuditStatus = AuditStatus.PENDIENTE,
    var notas: String ="",
    var fotoUri: String ?=null,
    var laboratorioId: String

):Parcelable

