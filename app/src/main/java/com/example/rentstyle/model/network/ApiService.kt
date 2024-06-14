package com.example.rentstyle.model.network

import com.example.rentstyle.model.data.response.ProductDetailResponse
import com.example.rentstyle.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("product/{id}")
    suspend fun getProductDetail(@Path("id") productId: String): Response<ProductDetailResponse>

    @GET("product/filter")
    suspend fun getFilteredProducts(
        @Query("key") filter: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<List<Product>>

    @Headers("Authorization: Sudah izin pada Wildan dan Yoga")
    @GET("product")
    suspend fun getProducts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<List<Product>>
}
