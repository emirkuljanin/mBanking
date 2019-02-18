package com.bank.mbank.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "users", primaryKeys = arrayOf("first_name", "last_name"))
data class User(
        @ColumnInfo(name = "first_name")
        var firstName: String,

        @ColumnInfo(name = "last_name")
        var lastName: String,

        @ColumnInfo(name = "pin")
        var pin: String?
)
