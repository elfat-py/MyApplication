package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log

@Database(entities = [Expense::class, User::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns a singleton instance of the database.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "some_db" // Database name
                )
                    .addCallback(AppDatabaseCallback(context)) // Pass context to the callback
                    .fallbackToDestructiveMigration() // For development; replace with migrations in production
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Database callback for handling events like prepopulation.
     */
    private class AppDatabaseCallback(private val context: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("AppDatabase", "Database created. Starting prepopulation...")
            prepopulateDatabase()
        }

        /**
         * Prepopulate the database with initial data.
         */
        private fun prepopulateDatabase() {
            // Use the INSTANCE directly after initialization
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val userDao = database.userDao()
                        if (userDao.getUserCount() == 0) { // Only prepopulate if no user exists
                            userDao.insertUser(
                                User(
                                    firstName = "Your First Name",
                                    lastName = "Second Name",
                                    monthlyBudget = 1000.0,
                                    currency = "USD" // Ensure a valid default value
                                )
                            )
                            Log.d("AppDatabase", "Prepopulation complete.")
                        } else {
                            Log.d("AppDatabase", "Database already contains user data. Skipping prepopulation.")
                        }
                    } catch (e: Exception) {
                        Log.e("AppDatabase", "Error during prepopulation: ${e.message}")
                    }
                }
            }
        }
    }
}
