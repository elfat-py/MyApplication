package com.example.myapplication

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.Expense
import com.example.myapplication.data.ExpenseDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExpenseDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ExpenseDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.expenseDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertExpense() = runBlocking {
        val expense = Expense(name = "Coffee", iconResId = 123, time = "08:00", date = "2025-01-25", amount = "2.50")
        dao.insertExpense(expense = expense)

        // Retrieve all expenses and verify
        val expenses = dao.getAllExpensesFlow().first() // .first() collects the first emission of the Flow
        assertThat(expenses.size, equalTo(1))
        assertThat(expenses[0].name, equalTo("Coffee"))
    }
}
