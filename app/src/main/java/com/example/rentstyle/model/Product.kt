package com.example.rentstyle.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: String,
    @SerializedName("product_name") val productName: String,
    val category: String,
    val color: String,
    val size: String,
    @SerializedName("rent_price") val rentPrice: Int,
    @SerializedName("count_num_rating") val countNumRating: Int,
    @SerializedName("avg_rating") val avgRating: Float
)
