package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.ExpenseRepository
import com.example.myapplication.viewmodel.ExpenseViewModel
import com.example.myapplication.viewmodel.ExpenseViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize DB, Repository, and ViewModel
        val db = AppDatabase.getDatabase(this)
        val repository = ExpenseRepository(db.expenseDao())
        val factory = ExpenseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ExpenseViewModel::class.java)

        // Setup RecyclerView
        recyclerView = findViewById(R.id.rvExpenses)
        adapter = ExpenseAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Collect Flow from ViewModel
        lifecycleScope.launchWhenStarted {
            viewModel.expenseListFlow.collectLatest { expenses ->
                adapter.submitList(expenses)
            }
        }

        // Add Expense button -> launch AddExpenseActivity
        findViewById<Button>(R.id.btnAddExpense).setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }

        // Load initial data
//        viewModel.loadExpenses()
    }
}
