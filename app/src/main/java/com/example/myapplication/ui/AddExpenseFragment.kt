package com.example.myapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MyApplication
import com.example.myapplication.R
import com.example.myapplication.viewmodel.ExpenseViewModel
import com.example.myapplication.viewmodel.ExpenseViewModelFactory

class AddExpenseFragment : Fragment() {

    private lateinit var viewModel: ExpenseViewModel

    private lateinit var nameEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var iconSpinner: Spinner
    private lateinit var saveButton: Button

    private val categories = listOf(
        CategoryItem("Food", R.drawable.ic_food),
        CategoryItem("Transport", R.drawable.ic_transport),
        CategoryItem("Shopping", R.drawable.ic_shopping)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_expense, container, false)

        // Bind views
        nameEditText = view.findViewById(R.id.editTextExpenseName)
        amountEditText = view.findViewById(R.id.editTextExpenseAmount)
        iconSpinner = view.findViewById(R.id.spinnerIcons)
        saveButton = view.findViewById(R.id.buttonSaveExpense)

        // Set up the custom spinner adapter
        val adapter = CategorySpinnerAdapter(requireContext(), categories)
        iconSpinner.adapter = adapter

        // Set up the ViewModel with a custom factory
        viewModel = ViewModelProvider(
            requireActivity(),
            ExpenseViewModelFactory(
                (requireActivity().application as MyApplication).expenseRepository
            )
        )[ExpenseViewModel::class.java]

        // Handle "Save Expense"
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val amount = amountEditText.text.toString().trim()

            // Get selected category & icon
            val selectedPosition = iconSpinner.selectedItemPosition
            val selectedItem = categories[selectedPosition]
            val iconResId = selectedItem.iconRes
            val categoryName = selectedItem.name  // in case you also want to store or display it

            // Insert the expense into the DB
            viewModel.insertExpense(
                name = name,
                iconResId = iconResId,
                amount = amount
            )

            Toast.makeText(requireContext(), "Expense saved!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        return view
    }
}

/** Data class for each category/spinner row. */
data class CategoryItem(val name: String, val iconRes: Int)

/** Custom adapter that inflates `spinner_item.xml` for each row. */
class CategorySpinnerAdapter(
    context: Context,
    private val items: List<CategoryItem>
) : ArrayAdapter<CategoryItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Called to display the selected item’s layout in the Spinner “preview”
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Called to display each row in the dropdown list
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)

        val iconImageView = itemView.findViewById<ImageView>(R.id.imageIcon)
        val nameTextView = itemView.findViewById<TextView>(R.id.textName)

        val item = items[position]
        iconImageView.setImageResource(item.iconRes)
        nameTextView.text = item.name

        return itemView
    }
}
