package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the User table.
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user where id = :userId")
    fun getUser(userId: Int): Flow<User>

    @Query("UPDATE user SET firstName = :firstName, lastName = :lastName, monthlyBudget = :monthlyBudget, currency = CASE WHEN :currency IS NOT NULL THEN :currency ELSE currency END, updated_at = :updatedAt WHERE id = :userId")
    suspend fun updateUser(
        userId: Int,
        firstName: String,
        lastName: String,
        currency: String?, // Nullable currency
        monthlyBudget: Double,
        updatedAt: Long
    )

}
