package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.Expense
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Expense table.
 */
@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expense ORDER BY date DESC")
    fun getAllExpensesFlow(): Flow<List<Expense>>

    @Query("DELETE FROM expense WHERE id = :expenseId")
    suspend fun deleteExpense(expenseId: Int)
}
