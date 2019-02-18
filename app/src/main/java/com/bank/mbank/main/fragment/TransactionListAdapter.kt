package com.bank.mbank.main.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bank.mbank.R
import com.bank.mbank.api.model.TransactionResponse
import kotlinx.android.synthetic.main.transaction_item.view.*

class TransactionListAdapter(
        private val transactions: List<TransactionResponse>,
        private val context: Context) : RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.transaction_item, parent, false))
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.amount.text = transactions[position].amount
        holder.date.text = transactions[position].date

        //In case a transaction has a type, we want to show that, so we do a check to format the description with type if it exists
        //We are suppressing this warning because for some reason the compiler thinks type can not be null
        @Suppress("SENSELESS_COMPARISON")
        if (transactions[position].type == null) {
            holder.description.text = transactions[position].description
        } else {
            holder.description.text = String.format(context.getString(R.string.transaction_type),
                    transactions[position].description,
                    transactions[position].type)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount = view.amount!!
        val date = view.date!!
        val description = view.description!!
    }
}