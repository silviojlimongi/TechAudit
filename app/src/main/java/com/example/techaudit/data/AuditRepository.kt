package com.example.techaudit.data

import com.example.techaudit.model.AuditItem
import kotlinx.coroutines.flow.Flow

class AuditRepository(private val AuditDao: AuditDao){
    val allItems: Flow<List<AuditItem>> = AuditDao.getAllItems()
    suspend fun insert(item: AuditItem){
        AuditDao.insertItem(item)
    }
    suspend fun update(item: AuditItem){
        AuditDao.updateItem(item)
    }
    suspend fun delete(item: AuditItem){
        AuditDao.delete(item)
    }


}
