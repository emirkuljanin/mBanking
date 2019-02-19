package com.bank.mbank.main.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bank.mbank.R
import com.bank.mbank.api.RetrofitUtil
import com.bank.mbank.api.model.StatementResponse
import com.bank.mbank.api.model.TransactionResponse
import com.bank.mbank.login.activity.LoginActivity
import com.bank.mbank.main.activity.MainActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionListFragment : Fragment() {
    private var transactionList: RecyclerView? = null
    private var accountBalance: TextView? = null
    private var progressContainer: FrameLayout? = null
    private var dateSpinner: Spinner? = null
    private var bankStatement: StatementResponse? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_transaction_list, container, false)

        setupFragment(rootView)
        return rootView
    }

    private fun setupFragment(rootView: View) {
        val activity = activity!! as MainActivity?
        val myAccountsButton = rootView.findViewById<TextView>(R.id.my_accounts_button)
        dateSpinner = rootView.findViewById(R.id.date_spinner)
        accountBalance = rootView.findViewById(R.id.account_balance)
        transactionList = rootView.findViewById(R.id.transactions_list)
        progressContainer = rootView.findViewById(R.id.progress_bar_container)

        setHeader(rootView, activity!!)

        transactionList!!.layoutManager = LinearLayoutManager(activity)
        myAccountsButton.setOnClickListener { onMyAccountsClick() }

        getTransactionList()
    }

    //Set header text and logout button on click listener
    private fun setHeader(rootView: View, activity: MainActivity) {
        val helloMessageTextView = rootView.findViewById<TextView>(R.id.hello_message)
        val logoutButton = rootView.findViewById<ImageView>(R.id.logout_button)

        logoutButton.setOnClickListener { logout() }

        doAsync {
            //Fetch user from database
            val database = activity.getDatabaseInstance(activity)
            val user = database.userDao().getUser()

            //Format header message
            val helloMessage = String.format(
                    getString(R.string.hello_user),
                    user.firstName,
                    user.lastName)

            uiThread {
                helloMessageTextView.text = helloMessage
            }
        }
    }

    //On logout just show LoginActivity so the user has to enter pin again
    private fun logout() {
        val activity = activity!! as MainActivity?
        activity!!.startActivity(activity, LoginActivity())
        activity.finish()
    }

    //Fetch data from API
    private fun getTransactionList() {
        doAsync {
            val callResponse = RetrofitUtil().getTransactionList()
            val response = callResponse.execute()

            //If call is successful process data, if not logout user as no method of retrying api call is specified in the project details
            if (response.isSuccessful) {
                bankStatement = response.body()
                uiThread { handleTransactionListData() }
            } else {
                Toast.makeText(activity!!, R.string.network_error, Toast.LENGTH_SHORT).show()
                logout()
            }
        }
    }

    //Handle bank statement data
    private fun handleTransactionListData() {
        //Since on start the user has no option to pink an account, we choose the first by default
        setupAccount(0)

        progressContainer!!.visibility = View.GONE
    }

    private fun onMyAccountsClick() {
        val accounts = ArrayList<String>()
        for (account in bankStatement!!.acounts) {
            accounts.add(account.IBAN)
        }

        //Set alert dialog with list of account numbers
        val array = accounts.toTypedArray()
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setTitle(getString(R.string.choose_account))
        alertBuilder.setItems(array) { _, which ->
            // Get the dialog selected item
            val selectedIndex = array.indexOf(array[which])

            //Set selected account to the adapter
            setupAccount(selectedIndex)
        }

        alertBuilder.show()
    }

    private fun setupAccount(index: Int) {
        val transactions = bankStatement!!.acounts[index].transactions
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val spinnerFormat = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())
        val spinnerList = ArrayList<String>()

        //Format date for better visibility and add to a list of spinner items
        for (transaction in transactions) {
            val formattedDate = spinnerFormat.format(formatter.parse(transaction.date))
            spinnerList.add(formattedDate)
        }

        //Set date(month) dropdown
        setDropDown(spinnerList, transactions)

        //Set recycler adapter
        setTransactionAdapter(transactions)

        //Set account balance text
        accountBalance!!.text = String.format(getString(R.string.concat_two_strings),
                bankStatement!!.acounts[index].amount,
                bankStatement!!.acounts[index].currency)
    }

    private fun setDropDown(spinnerList: ArrayList<String>, transactions: List<TransactionResponse>) {
        val adapter = ArrayAdapter(activity!!,
                R.layout.dropdown_item,
                spinnerList.distinct())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dateSpinner!!.adapter = adapter
        dateSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Nothing to do here
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Update transactions
                setTransactionAdapter(transactions)
            }
        }
    }

    private fun setTransactionAdapter(transactions: List<TransactionResponse>) {
        val transactionsForSelectedMonth = ArrayList<TransactionResponse>()

        for (transaction in transactions) {
            if (isSelectedMonth(transaction.date)) {
                transactionsForSelectedMonth.add(transaction)
            }
        }

        transactionList!!.adapter = TransactionListAdapter(
                transactionsForSelectedMonth,
                activity!!)
    }

    private fun isSelectedMonth(date: String): Boolean {
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val spinnerFormat = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())

        return spinnerFormat.format(formatter.parse(date)) == dateSpinner!!.selectedItem as String
    }
}
