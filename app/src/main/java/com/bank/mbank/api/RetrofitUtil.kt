package com.bank.mbank.api

import com.bank.mbank.api.model.StatementResponse
import com.bank.mbank.api.service.ApiService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitUtil {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://mobile.asseco-see.hr")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getTransactionList(): Call<StatementResponse> {
        return apiService.getTransactionList()
    }
}
