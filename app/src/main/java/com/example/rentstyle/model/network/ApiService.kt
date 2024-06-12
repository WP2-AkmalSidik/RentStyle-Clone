package com.example.rentstyle.model.network

import com.example.rentstyle.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("product")
    fun getProducts(): Call<List<Product>>
}
