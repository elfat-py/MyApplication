package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Expense Entity mapped to 'expense' table in the database.
 */
@Entity(tableName = "expense")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L, // Auto-generated primary key
    val name: String,
    val iconResId: Int, // Resource ID for the icon
    val time: String,
    val date: String,
    val amount: String
)