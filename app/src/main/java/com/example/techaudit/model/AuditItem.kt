package com.example.techaudit.model

//AuditItem
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import java.util.Date


import androidx.room.PrimaryKey

@Entity(tableName = "equipos")
@Parcelize
data class AuditItem ( // para pasar los datos entre pantallas activities

    @PrimaryKey
    val id: String, // uuid o codigo de barras


    val nombre: String,
    val ubicacion: String,
    val fechaRegistro: String,
    var estado: AuditStatus = AuditStatus.PENDIENTE,
    var notas: String ="",
    var fotoUri: String ?=null

):Parcelable