package com.bank.mbank.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.room.Room
import com.bank.mbank.R
import com.bank.mbank.database.AppDatabase

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    //To avoid boilerplate code we've added a simple extension function to fragment manager
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction { add(R.id.fragment_container, fragment) }
    }

    fun getDatabaseInstance(context: Context): AppDatabase {
        return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "users").build()
    }

    fun startActivity(context: Activity, activity: Activity) {
        val intent = Intent(context, activity::class.java)

        startActivity(intent)
    }
}
