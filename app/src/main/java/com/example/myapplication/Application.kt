package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.ExpenseRepository

class MyApplication : Application() {

    // Lazy init so they're created only when first needed
    val database by lazy { AppDatabase.getDatabase(this) }
    val expenseRepository by lazy { ExpenseRepository(database.expenseDao()) }
}
