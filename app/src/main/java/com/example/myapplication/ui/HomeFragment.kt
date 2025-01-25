package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MyApplication
import com.example.myapplication.R
import com.example.myapplication.viewmodel.ExpenseViewModel
import com.example.myapplication.viewmodel.ExpenseViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var expenseViewModel: ExpenseViewModel

    // Recycler
    private lateinit var recyclerView: RecyclerView
    private val expenseAdapter = ExpenseAdapter()

    // Summaries
    private lateinit var housingText: TextView
    private lateinit var foodText: TextView
    private lateinit var funText: TextView
    private lateinit var budgetText: TextView
    private lateinit var spentText: TextView

    // Hard-coded budget (or fetch from somewhere else)
    private val monthlyBudget = 2000.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Bind views for categories
        housingText = view.findViewById(R.id.housingText)
        foodText = view.findViewById(R.id.foodText)
        funText = view.findViewById(R.id.funText)
        budgetText = view.findViewById(R.id.budgetText)
        spentText = view.findViewById(R.id.spentText)

        // Show the monthly budget
        budgetText.text = "$$monthlyBudget"

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.expenses_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = expenseAdapter

        // Get ViewModel
        val application = requireActivity().application as MyApplication
        val viewModelFactory = ExpenseViewModelFactory(application.expenseRepository)
        expenseViewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ExpenseViewModel::class.java)

        // Observe DB
        expenseViewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            // Update the spending list
            expenseAdapter.submitList(expenses)

            // Convert amounts from string to double
            // (If your DB has currency symbols in the string, remove them,
            // or store amounts as Double in the DB for simplicity)
            val parsedExpenses = expenses.map { e ->
                // Remove '$' if present, then parse
                val numericAmount = e.amount.replace("$","").toDoubleOrNull() ?: 0.0
                e to numericAmount
            }

            // 1) Housing
            val housingSum = parsedExpenses
                .filter { (exp, _) ->
                    exp.name.equals("Housing", ignoreCase = true)
                }
                .sumOf { it.second }  // sum of amounts
            housingText.text = "$$housingSum"

            // 2) Food
            val foodSum = parsedExpenses
                .filter { (exp, _) ->
                    exp.name.equals("Food", ignoreCase = true)
                            || exp.name.equals("Groceries", ignoreCase = true)
                }
                .sumOf { it.second }
            foodText.text = "$$foodSum"

            // 3) Fun
            val funSum = parsedExpenses
                .filter { (exp, _) ->
                    exp.name.equals("Fun", ignoreCase = true)
                            || exp.name.equals("Entertainment", ignoreCase = true)
                }
                .sumOf { it.second }
            funText.text = "$$funSum"

            // 4) Total spent (all expenses combined)
            val totalSpent = parsedExpenses.sumOf { it.second }
            spentText.text = "$$totalSpent"
        }

        return view
    }
}
