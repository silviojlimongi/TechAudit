package com.example.techaudit.ui
//AuditViewModel.kt
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.techaudit.TechAuditApp
import com.example.techaudit.data.AuditRepository
import com.example.techaudit.model.AuditItem
import kotlinx.coroutines.launch

class AuditViewModel (application: Application) : AndroidViewModel(application){
    private val repository: AuditRepository
    val allItems: LiveData<List<AuditItem>>

    init{
        val dao = (application as TechAuditApp).database.auditDao()
        repository = AuditRepository(dao)
        allItems = repository.allItems.asLiveData()
    }
    fun insert(item: AuditItem) = viewModelScope.launch{
        repository.insert(item)
    }
    fun update(item: AuditItem) = viewModelScope.launch{
        repository.update(item)
    }
    fun delete(item: AuditItem) = viewModelScope.launch{

        repository.delete(item)
    }
}
