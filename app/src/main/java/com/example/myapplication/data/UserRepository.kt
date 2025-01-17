package com.example.myapplication.data

class UserRepository(private val userDao: UserDao) {

    val currentUser = userDao.getUser(1) // This has been left to 1 as aren't going to support multiple users atm

    suspend fun addUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user.id, user.firstName, user.lastName, user.monthlyBudget, System.currentTimeMillis())
    }


}