package com.example.myapplication.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.Expense
import com.example.myapplication.data.ExpenseRepository
import com.example.myapplication.viewmodel.ExpenseViewModel
import com.example.myapplication.viewmodel.ExpenseViewModelFactory

class AddExpenseActivity : ComponentActivity() {

    private lateinit var viewModel: ExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        // Setup ViewModel
        val db = AppDatabase.getDatabase(this)
        val repository = ExpenseRepository(db.expenseDao())
        val factory = ExpenseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ExpenseViewModel::class.java)

        val etDate = findViewById<EditText>(R.id.etDate)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val etCategory = findViewById<EditText>(R.id.etCategory)
        val etNotes = findViewById<EditText>(R.id.etNotes)
        val btnSave = findViewById<Button>(R.id.btnSaveExpense)

        btnSave.setOnClickListener {
            val dateStr = etDate.text.toString()
            val amountStr = etAmount.text.toString()
            val categoryStr = etCategory.text.toString()
            val notesStr = etNotes.text.toString()

            if (dateStr.isEmpty() || amountStr.isEmpty() || categoryStr.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please fill date, amount, and category!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val expense = Expense(
                date = dateStr,
                amount = amountStr.toDoubleOrNull() ?: 0.0,
                category = categoryStr,
                notes = notesStr
            )

            // Insert expense via ViewModel
            viewModel.addExpense(expense)
            finish() // Go back to MainActivity
        }
    }
}
