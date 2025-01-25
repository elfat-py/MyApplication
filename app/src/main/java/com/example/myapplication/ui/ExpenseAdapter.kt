package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Expense

/**
 * RecyclerView Adapter for Expense items.
 */

class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var expenses: List<Expense> = emptyList()

    fun submitList(newExpenses: List<Expense>) {
        // Replace old data
        expenses = newExpenses
        // Notify the adapter that the data changed
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_item, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.icon.setImageResource(expense.iconResId)
        holder.name.text = expense.name
        holder.timeDate.text = "${expense.time} • ${expense.date}"
        holder.amount.text = expense.amount
    }

    override fun getItemCount(): Int = expenses.size

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.expense_icon)
        val name: TextView = view.findViewById(R.id.expense_name)
        val timeDate: TextView = view.findViewById(R.id.expense_time_date)
        val amount: TextView = view.findViewById(R.id.expense_amount)
    }
}
