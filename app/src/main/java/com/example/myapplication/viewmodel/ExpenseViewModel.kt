package com.example.myapplication.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.launch
import com.example.myapplication.data.Expense
import com.example.myapplication.data.ExpenseRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * ViewModel for managing expense data.
 */
class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    // Observing the list of expenses
    val allExpenses: LiveData<List<Expense>> = repository.allExpensesFlow.asLiveData()

    // Insert a new expense
    @SuppressLint("NewApi")
    fun insertExpense(name: String, iconResId: Int, amount: String) {
        viewModelScope.launch {
            val currentDateTime = LocalDateTime.now()
            val date = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val time = currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

            val expense = Expense(
                name = name,
                iconResId = iconResId,
                time = time,
                date = date,
                amount = amount
            )
            repository.addExpense(expense)
        }
    }
}
