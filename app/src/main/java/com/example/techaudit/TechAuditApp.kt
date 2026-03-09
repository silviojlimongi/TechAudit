package com.example.techaudit
//TechAuditApp.kt

import android.app.Application
import com.example.techaudit.data.AuditDatabase


class TechAuditApp: Application() {
    //la base de datos solo se crea cuando alguien la pride por primera vez
    val database by lazy { AuditDatabase.getDatabase(this) }

}