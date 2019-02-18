package com.bank.mbank.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bank.mbank.R
import com.bank.mbank.database.model.User
import com.bank.mbank.login.activity.LoginActivity
import com.bank.mbank.main.activity.MainActivity
import com.bank.mbank.widget.CustomKeyboard
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class PinFragment : DialogFragment() {
    private var pinEditText: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_pin, container, false)

        setupFragment(rootView)
        return rootView
    }

    private fun setupFragment(rootView: View) {
        val activity = activity!! as LoginActivity?

        pinEditText = rootView.findViewById(R.id.pin_edit_text)
        val keyboard = rootView.findViewById<CustomKeyboard>(R.id.keyboard)
        val loginButton = rootView.findViewById<TextView>(R.id.login_button)

        //Assign the pin edit text to the keyboard for input
        val ic = pinEditText!!.onCreateInputConnection(EditorInfo())
        keyboard.setInputConnection(ic)

        loginButton.setOnClickListener { onButtonClick(activity!!) }
    }

    private fun onButtonClick(activity: LoginActivity) {
        //Show toast error if pin is shorter than 4 characters
        if (pinEditText!!.text.length < 4) {
            Toast.makeText(activity, R.string.pin_length, Toast.LENGTH_SHORT).show()
            return
        }

        //Fetch user from db
        doAsync {
            val database = activity.getDatabaseInstance(activity)
            val user = database.userDao().getUser()
            uiThread {
                handleUserData(activity, user)
            }
        }
    }

    private fun handleUserData(activity: LoginActivity, user: User) {
        val database = activity.getDatabaseInstance(activity)

        //If user has not registered yet do not check pin, and write the entered value to db
        if (user.pin == null) {
            user.pin = pinEditText!!.text.toString()
            doAsync {
                //Update user object in database with pin
                database.userDao().updateUser(user)

                //Start new activity and show transaction list
                activity.startActivity(activity, MainActivity())
            }
        }

        //If pin is entered correctly transaction list is shown else we show error message
        if (pinEditText!!.text.toString() == user.pin)
            activity.startActivity(activity, MainActivity()) else
            Toast.makeText(activity, R.string.pin_bad, Toast.LENGTH_SHORT).show()

        this.dismiss()
    }
}
