package com.bank.mbank.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bank.mbank.R
import com.bank.mbank.database.model.User
import com.bank.mbank.login.activity.LoginActivity
import org.jetbrains.anko.doAsync

class RegistrationFragment : Fragment() {
    private var firstNameView: EditText? = null
    private var lastNameView: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_registration, container, false)

        setupFragment(rootView)

        return rootView
    }

    private fun setupFragment(rootView: View) {
        val activity = activity!! as LoginActivity?

        firstNameView = rootView.findViewById(R.id.edit_text_first_name)
        lastNameView = rootView.findViewById(R.id.edit_text_last_name)
        val continueButton = rootView.findViewById<TextView>(R.id.continue_button)

        continueButton.setOnClickListener { onButtonClick(activity!!) }
    }

    private fun onButtonClick(activity: LoginActivity) {
        //If there are no values for first or last name we do not allow the user to proceed
        if (firstNameView!!.text.isEmpty() or lastNameView!!.text.isEmpty()) {
            Toast.makeText(activity, R.string.enter_details_error, Toast.LENGTH_SHORT).show()
            return
        }

        //Write user information to database
        doAsync {
            activity.getDatabaseInstance(activity).userDao().insertUser(User(firstNameView!!.text.toString(),
                    lastNameView!!.text.toString(),
                    null))
        }

        //Open pin dialog
        PinFragment().show(activity.supportFragmentManager, "pinDialog")
    }
}