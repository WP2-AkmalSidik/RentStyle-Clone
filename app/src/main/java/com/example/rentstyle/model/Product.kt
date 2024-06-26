package com.example.rentstyle.model

import com.example.rentstyle.model.data.local.LocalProduct
import com.example.rentstyle.model.data.response.Review
import com.google.gson.annotations.SerializedName

data class Product(
    val id: String,
    @SerializedName("product_id") val productId: String?,  // Nullable String for product_id
    @SerializedName("product_name") val productName: String,
    val category: String,
    val color: String,
    val size: String,
    val city: String,
    @SerializedName("image") val image: String?, // Nullable String
    @SerializedName("rent_price") val rentPrice: Int,
    @SerializedName("count_num_rating") val countNumRating: Int,
    @SerializedName("avg_rating") val avgRating: Float,
    @SerializedName("reviews") val reviews: List<Review>? // Optional
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
            avgRating = avgRating,
            city = city,
            image = image
        )
    }
}
