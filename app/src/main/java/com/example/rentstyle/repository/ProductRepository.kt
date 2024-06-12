package com.example.rentstyle.repository

import com.example.rentstyle.model.Product
import com.example.rentstyle.model.network.ApiConfig
import retrofit2.Call

class ProductRepository {
    private val apiService = ApiConfig.getApiService()

    suspend fun fetchProducts(): Call<List<Product>> {
        return apiService.getProducts()
    }
}
