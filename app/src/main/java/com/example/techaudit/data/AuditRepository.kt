package com.example.techaudit.data
//AuditRepository


import androidx.room.Delete
import com.example.techaudit.model.AuditItem
import kotlinx.coroutines.flow.Flow

class AuditRepository(private val auditDao: AuditDao){
    val allItems: Flow<List<AuditItem>> = auditDao.getAllItems()
    suspend fun insert(item: AuditItem){
        auditDao.insertItem(item)
    }
    suspend fun update(item: AuditItem){
        auditDao.updateItem(item)
    }
    suspend fun delete(item: AuditItem) {
        auditDao.deleteItem(item)
    }

}
