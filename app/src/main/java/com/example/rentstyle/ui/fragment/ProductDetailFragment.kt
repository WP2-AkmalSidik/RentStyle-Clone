package com.example.rentstyle.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentstyle.databinding.FragmentProductDetailBinding
import com.example.rentstyle.helpers.adapter.ReviewDummyAdapter
import com.example.rentstyle.model.Product
import com.example.rentstyle.model.data.response.ProductDetailResponse
import com.example.rentstyle.model.network.ApiConfig
import kotlinx.coroutines.launch
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.rentstyle.R

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewAdapter: ReviewDummyAdapter // Deklarasikan reviewAdapter di sini

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        reviewAdapter = ReviewDummyAdapter()

        binding.rvProductRating.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        fetchProductDetail()

        return binding.root
    }

    private fun fetchProductDetail() {
        val productId = arguments?.getString("productId")
        if (productId == null) {
            Log.e("ProductDetailFragment", "Product ID is null")
            return
        }

        val apiService = ApiConfig.getApiService()
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = apiService.getProductDetail(productId)
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        bindProductData(product)
                    } else {
                        Log.e("ProductDetailFragment", "Empty product detail response")
                    }
                } else {
                    Log.e("ProductDetailFragment", "Failed to load product detail: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ProductDetailFragment", "Failed to load product detail: ${e.message}", e)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindProductData(product: ProductDetailResponse) {
        binding.apply {
            Glide.with(requireContext())
                .load(product.image)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_placeholder)
                )
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivProductImage)
            tvProductName.text = product.productName
            tvProductPrice.text = "Rp ${product.rentPrice} / hari"
            tvProductRating.text =
                "Rating: %.1f".format(product.reviews.map { it.rating }.average())
            tvProductShopName.text = product.seller.sellerName
            tvShopLocation.text = product.seller.city
            tvProductCategory.text = product.category
            tvProductColor.text = product.color
            tvProductSize.text = product.size
            tvProductDescription.text = product.desc

            reviewAdapter = ReviewDummyAdapter().apply {
                updateReviews(product.reviews)
            }
            rvProductRating.adapter = reviewAdapter
            rvProductRating.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
