package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Expense
import com.example.myapplication.data.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing expense data.
 */
class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    /**
     * Load all expenses from the repository.
     */
    val expenseListFlow: StateFlow<List<Expense>> = repository.allExpensesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Add a new expense to the database and refresh.
     */
    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.addExpense(expense)

        }
    }

    /**
     * Delete an expense by ID and refresh.
     */
    fun deleteExpense(id: Int) {
        viewModelScope.launch {
            repository.deleteExpense(id)

        }
    }
}
