package com.example.rentstyle.model.data.response

import com.google.gson.annotations.SerializedName

data class Seller(
    @SerializedName("seller_name") val sellerName: String,
    @SerializedName("city") val city: String
)