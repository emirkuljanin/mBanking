package com.bank.mbank.api.model

class AccountsResponse(
        val id: String,
        val IBAN: String,
        val amount: String,
        val currency: String,
        val transactions: List<TransactionResponse>)