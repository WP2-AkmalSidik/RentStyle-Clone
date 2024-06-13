package com.example.rentstyle.model

import com.example.rentstyle.model.data.local.LocalProduct
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
) {
    fun toLocalProduct(): LocalProduct {
        return LocalProduct(
            id = id,
            productName = productName,
            category = category,
            color = color,
            size = size,
            rentPrice = rentPrice,
            countNumRating = countNumRating,
            avgRating = avgRating
        )
    }
}
