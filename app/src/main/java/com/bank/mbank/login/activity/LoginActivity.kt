package com.bank.mbank.login.activity

import android.os.Bundle
import com.bank.mbank.R
import com.bank.mbank.login.fragment.LoginFragment
import com.bank.mbank.BaseActivity

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initialize()
    }

    //Populate the fragment container with the Login screen
    private fun initialize() {
        addFragment(LoginFragment())
    }
}
