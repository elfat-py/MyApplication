package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository mediates between the DAO and the ViewModel.
 */
class ExpenseRepository(private val expenseDao: ExpenseDao) {

    val allExpensesFlow: Flow<List<Expense>> = expenseDao.getAllExpensesFlow()

    suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }



    suspend fun deleteExpense(id: Int) {
        expenseDao.deleteExpense(id)
    }
}
