package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val monthlyBudget: Double,
    val currency: String = "USD",
    val created_at: Long = System.currentTimeMillis(), // Store as epoch time (milliseconds)
    val updated_at: Long = System.currentTimeMillis()  // Store as epoch time (milliseconds)

)
