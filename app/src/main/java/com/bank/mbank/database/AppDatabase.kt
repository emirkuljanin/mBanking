package com.bank.mbank.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bank.mbank.database.dao.UserDao
import com.bank.mbank.database.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}