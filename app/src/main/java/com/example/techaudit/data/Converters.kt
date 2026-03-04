package com.example.techaudit.data
//Converters
import androidx.room.TypeConverter
import com.example.techaudit.model.AuditStatus
//Room no guarda enums directo (a menos que lo conviertas). Lo más simple: guardar como String.

class Converters {

    // de Enum  a string

    @TypeConverter
    fun fromStatus(status: AuditStatus): String {
        return status.name
    }



    // de string a Enum
    @TypeConverter
    fun toStatus(value:String): AuditStatus{
        return AuditStatus.values().firstOrNull { it.name == value } ?: AuditStatus.PENDIENTE
    }
}