package com.bank.mbank.main.activity

import android.os.Bundle
import com.bank.mbank.R
import com.bank.mbank.main.fragment.TransactionListFragment
import com.bank.mbank.widget.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
    }

    //Populate the fragment container with the Transaction List screen
    private fun initialize() {
        addFragment(TransactionListFragment())
    }
}
