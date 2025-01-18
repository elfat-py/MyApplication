package com.example.myapplication.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.User
import com.example.myapplication.data.UserRepository
import kotlinx.coroutines.launch

class UserFragment : Fragment() {

    private lateinit var userRepository: UserRepository
    private lateinit var firstNameView: TextView
    private lateinit var lastNameView: TextView
    private lateinit var budgetView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        // Initialize views
        firstNameView = view.findViewById(R.id.first_name_value)
        lastNameView = view.findViewById(R.id.last_name_value)
        budgetView = view.findViewById(R.id.monthly_budget_value)

        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val currencySpinner = view.findViewById<Spinner>(R.id.currency_dropdown)

        // Initialize UserRepository
        userRepository = UserRepository(userDao) // Adjust this as per your DI setup
        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCurrency = parent?.getItemAtPosition(position).toString()

                // Update currency in the database
                lifecycleScope.launch {
                    userRepository.currentUser.collect { user ->
                        user?.let {
                            if (it.currency != selectedCurrency) {
                                userRepository.updateUser(
                                    it.copy(currency = selectedCurrency)
                                )
                                Toast.makeText(
                                    requireContext(),
                                    "Currency updated to $selectedCurrency",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Observe user data
        observeUserData()

        // Handle edit button click
        // Handle edit button click
        view.findViewById<View>(R.id.edit_user_button).setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_user, null)
            val firstNameInput = dialogView.findViewById<EditText>(R.id.edit_first_name)
            val lastNameInput = dialogView.findViewById<EditText>(R.id.edit_last_name)
            val budgetInput = dialogView.findViewById<EditText>(R.id.edit_budget)

            // Populate current user data
            lifecycleScope.launch {
                userRepository.currentUser.collect { user ->
                    user?.let {
                        firstNameInput.setText(it.firstName)
                        lastNameInput.setText(it.lastName)
                        budgetInput.setText(it.monthlyBudget.toString())
                    }
                }
            }

            // Build the AlertDialog
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Edit Profile")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    // Get updated user information
                    val updatedFirstName = firstNameInput.text.toString()
                    val updatedLastName = lastNameInput.text.toString()
                    val updatedBudget = budgetInput.text.toString().toDoubleOrNull() ?: 0.0

                    // Update user in the database
                    lifecycleScope.launch {
                        userRepository.currentUser.collect { user ->
                            user?.let {
                                userRepository.updateUser(
                                    it.copy(
                                        firstName = updatedFirstName,
                                        lastName = updatedLastName,
                                        monthlyBudget = updatedBudget,
                                        currency = currencySpinner.selectedItem.toString(),
                                    )
                                )
                            }
                        }
                    }
                    Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
        return view
    }

    private fun observeUserData() {
        lifecycleScope.launch {
            userRepository.currentUser.collect { user ->
                updateUserUI(user)
            }
        }
    }

    private fun updateUserUI(user: User) {
        firstNameView.text = user.firstName
        lastNameView.text = user.lastName
        budgetView.text = "${user.monthlyBudget}"

    }
}
