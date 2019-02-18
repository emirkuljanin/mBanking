package com.bank.mbank.database.dao

import androidx.room.*
import com.bank.mbank.database.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users LIMIT 1")
    fun getUser() : User

    @Insert
    fun insertUser(vararg user: User)

    @Update
    fun updateUser(vararg users: User)

    @Query("DELETE FROM users")
    fun deleteUser()
}