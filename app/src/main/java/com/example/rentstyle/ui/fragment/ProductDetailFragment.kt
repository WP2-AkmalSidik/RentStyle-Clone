package com.example.rentstyle.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.rentstyle.R
import com.example.rentstyle.databinding.FragmentProductDetailBinding
import com.example.rentstyle.helpers.adapter.ImageSliderAdapter
import com.example.rentstyle.helpers.adapter.ReviewDummyAdapter
import com.example.rentstyle.model.data.response.ProductDetailResponse
import com.example.rentstyle.model.network.ApiConfig
import kotlinx.coroutines.launch

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var carouselAdapter: ImageSliderAdapter
    private lateinit var reviewAdapter: ReviewDummyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        // Setup toolbar
        binding.mainToolbar.tvToolbarTitle.text = "Product Detail"
        binding.mainToolbar.ivBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        setUpImageCarousel()
        fetchProductDetail()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpImageCarousel() {
        val imageList = arrayListOf(R.drawable.img_placeholder, R.drawable.img_placeholder, R.drawable.img_placeholder)
        carouselAdapter = ImageSliderAdapter(requireContext(), imageList, "Product")

        binding.vpProductImageCarousel.apply {
            adapter = carouselAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun fetchProductDetail() {
        val productId = arguments?.getString("productId") ?: return
        val apiService = ApiConfig.getApiService()

        // Using coroutine to call the suspend function
        lifecycleScope.launch {
            try {
                val product = apiService.getProductDetail(productId)
                bindProductData(product)
            } catch (e: Exception) {
                // Handle the error
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindProductData(product: ProductDetailResponse) {
        binding.apply {
            tvProductName.text = product.productName
            tvProductPrice.text = "Rp ${product.rentPrice} / hari"

            val averageRating = if (product.reviews.isNotEmpty()) {
                product.reviews.map { it.rating }.average()
            } else {
                0.0
            }
            tvProductRating.text = "Rating %.1f".format(averageRating)
            tvProductShopName.text = product.seller.sellerName
            tvShopLocation.text = product.seller.city
            tvProductCategory.text = product.category
            tvProductColor.text = product.color
            tvProductSize.text = product.size
            tvProductDescription.text = product.desc

            // Initialize and set up RecyclerView for reviews
            reviewAdapter = ReviewDummyAdapter().apply {
                updateReviews(product.reviews)
            }
            rvProductRating.adapter = reviewAdapter
            rvProductRating.layoutManager = LinearLayoutManager(context)
        }
    }
}
