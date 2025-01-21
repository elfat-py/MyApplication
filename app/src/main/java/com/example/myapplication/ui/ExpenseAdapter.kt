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
//class ExpenseAdapter : ListAdapter<Expense, ExpenseAdapter.ViewHolder>(ExpenseDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater.inflate(R.layout.item_expense, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val expense = getItem(position)
//        holder.bind(expense)
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
//        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
//        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
//        private val tvNotes: TextView = itemView.findViewById(R.id.tvNotes)
//
//        fun bind(expense: Expense) {
//            tvDate.text = expense.date
//            tvAmount.text = expense.amount.toString()
//            tvCategory.text = expense.category
//            tvNotes.text = expense.notes ?: ""
//        }
//    }
//}

//class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
//    override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
//        return oldItem == newItem
//    }
//}


class ExpenseAdapter(private val expenses: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.expense_icon)
        val name: TextView = view.findViewById(R.id.expense_name)
        val timeDate: TextView = view.findViewById(R.id.expense_time_date)
        val amount: TextView = view.findViewById(R.id.expense_amount)
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
}
