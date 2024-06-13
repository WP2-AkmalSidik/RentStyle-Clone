package com.example.rentstyle.model.network

import com.example.rentstyle.model.Product
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("product")
    suspend fun getProducts(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<Product>

    @GET("product/filter")
    suspend fun getFilteredProducts(
        @Query("key") filter: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<Product>
}
