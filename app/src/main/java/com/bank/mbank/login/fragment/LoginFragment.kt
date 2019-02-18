package com.bank.mbank.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bank.mbank.R
import com.bank.mbank.database.model.User
import com.bank.mbank.login.activity.LoginActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LoginFragment : Fragment() {
    var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        setupFragment(rootView)

        return rootView
    }

    private fun setupFragment(rootView: View) {
        val activity = activity!! as LoginActivity?

        val loginDescription = rootView.findViewById<TextView>(R.id.login_description)
        val loginButton = rootView.findViewById<TextView>(R.id.login_button)

        loginButton.setOnClickListener { onButtonClick(activity!!) }

        //Fetch user from database
        doAsync {
            user = activity!!.getDatabaseInstance(activity).userDao().getUser()
            uiThread {
                //Set UI text
                loginButton.text = getLoginButtonText()
                loginDescription.text = getDescriptionString()
            }
        }
    }

    //Get text for login/registration button based on user registration status
    private fun getLoginButtonText(): String {
        if ((user == null) or (user?.pin == null)) {
            return getString(R.string.button_register)
        }

        return getString(R.string.button_login)
    }


    //Set our description based on whether the user has registered on this device
    private fun getDescriptionString(): String {
        if ((user == null) or (user?.pin == null)) {
            return getString(R.string.register_description)
        }

        return String.format(getString(R.string.login_description), user!!.firstName, user!!.lastName)
    }

    private fun onButtonClick(activity: LoginActivity) {
        //Depending on the user registration status we navigate to either Pin Entry or Registration
        if ((user != null) and (user?.pin != null)) {
            PinFragment().show(activity.supportFragmentManager, "pinDialog")
        } else {
            //If the user closed the app after entering name, we delete it from database to avoid multiple entries
            doAsync {
                activity.getDatabaseInstance(activity).userDao().deleteUser()
            }
            activity.addFragment(RegistrationFragment())
        }
    }
}
