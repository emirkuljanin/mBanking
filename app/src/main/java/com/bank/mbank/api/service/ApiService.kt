package com.bank.mbank.api.service

import com.bank.mbank.api.model.StatementResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("/builds/ISBD_public/Zadatak_1.json")
    fun getTransactionList(): Call<StatementResponse>
}