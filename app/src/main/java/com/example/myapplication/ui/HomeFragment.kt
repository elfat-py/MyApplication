package com.example.myapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Expense


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Sample data for expenses
        val expenses = listOf(
            Expense(0L, "Spotify", R.drawable.ic_food, "10:00 am", "Mar 26th 2023", "$54.99"),
            Expense(0L, "Figma", R.drawable.ic_sailing, "8:00 am", "Mar 21st 2023", "$8.99"),
            Expense(0L, "Online Shopping", R.drawable.ic_coffe, "10:00 am", "Mar 11th 2023", "$132.00"),
            Expense(0L, "AirBnB Rent", R.drawable.ic_snowmobile, "11:00 am", "Mar 2nd 2023", "$548.99")
        )

        // Initialize RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.expenses_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ExpenseAdapter(expenses)

        return view
    }
}
