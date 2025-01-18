package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The Room database class.
 * Defines the tables (entities) and the version.
 */
@Database(entities = [Expense::class, User::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Get or create the database instance.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todo_db_db" // Database name
                )
                    .addCallback(AppDatabaseCallback(context)) // Use callback for prepopulation
                    .fallbackToDestructiveMigration() // For schema changes during development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Room Callback for database operat
     *     private fun updateUserUI(user: User) {
     *         firstNameView.text = user.firstName
     *         lastNameView.text = user.lastName
     *         budgetView.text = "${user.monthlyBudget}"
     *
     *     }ions like pre-populating data.
     */
    private class AppDatabaseCallback(private val context: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            prepopulateDatabase(context)
        }

        /**
         * Prepopulate the database with a default user.
         */
        private fun prepopulateDatabase(context: Context) {
            val database = getDatabase(context)
            CoroutineScope(Dispatchers.IO).launch {
                database.userDao().insertUser(
                    User(
                        firstName = "Your First Name",
                        lastName = "Second Name",
                        monthlyBudget = 1000.0,
                        currency = "USD"
                    )
                )
            }
        }
    }
}
