package com.example.rentstyle.ui.fragment

import ProductAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.rentstyle.R
import com.example.rentstyle.databinding.FragmentForYouBinding
import com.example.rentstyle.helpers.GridSpacingItemDecoration
import com.example.rentstyle.helpers.adapter.ImageSliderAdapter
import com.example.rentstyle.model.Product
import com.example.rentstyle.model.network.ApiConfig
import com.example.rentstyle.ui.fragment.HomeFragmentDirections
import kotlinx.coroutines.launch

class ForYouFragment : Fragment() {
    private lateinit var _binding: FragmentForYouBinding
    private val binding get() = _binding

    private lateinit var carousel: ViewPager2
    private lateinit var carouselAdapter: ImageSliderAdapter

    private lateinit var highestRatingRecyclerView: RecyclerView
    private lateinit var newProductRecyclerView: RecyclerView
    private lateinit var recommendationProductRecyclerView: RecyclerView
    private lateinit var highestRatingAdapter: ProductAdapter
    private lateinit var newProductAdapter: ProductAdapter
    private lateinit var recommendationProductAdapter: ProductAdapter

    private val handler = Handler(Looper.getMainLooper())
    private val slideRunnable = Runnable {
        if (carousel.currentItem == 2) {
            carousel.currentItem = 0
        } else {
            carousel.currentItem += 1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForYouBinding.inflate(inflater, container, false)

        createCarouselInstance()
        createProductRecyclerViewInstance()
        fetchFilteredProducts()
        return binding.root
    }

    private fun createProductRecyclerViewInstance() {
        binding.apply {
            highestRatingRecyclerView = rvHighestRating
            newProductRecyclerView = rvNewProduct
            recommendationProductRecyclerView = rvRecommendation
        }

        highestRatingAdapter = ProductAdapter(emptyList())
        newProductAdapter = ProductAdapter(emptyList())
        recommendationProductAdapter = ProductAdapter(emptyList())

        recommendationProductRecyclerView.addItemDecoration(GridSpacingItemDecoration(2, 25, true))

        highestRatingRecyclerView.adapter = highestRatingAdapter
        newProductRecyclerView.adapter = newProductAdapter
        recommendationProductRecyclerView.adapter = recommendationProductAdapter

        highestRatingAdapter.setOnClickListener(object : ProductAdapter.OnClickListener {
            override fun onClick(position: Int, image: ImageView) {
                val extras = FragmentNavigatorExtras(image to "shared_product_image")
                findNavController().navigate(
                    HomeFragmentDirections.actionNavigationHomeToNavigationProductDetail(),
                    navigatorExtras = extras
                )
            }
        })

        newProductAdapter.setOnClickListener(object : ProductAdapter.OnClickListener {
            override fun onClick(position: Int, image: ImageView) {
                findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigationProductDetail())
            }
        })

        recommendationProductAdapter.setOnClickListener(object : ProductAdapter.OnClickListener {
            override fun onClick(position: Int, image: ImageView) {
                findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigationProductDetail())
            }
        })
    }

    private fun createCarouselInstance() {
        carousel = binding.vpCarousel

        val imageList = arrayListOf(
            R.drawable.img_placeholder,
            R.drawable.img_placeholder,
            R.drawable.img_placeholder
        )
        carouselAdapter = ImageSliderAdapter(requireContext(), imageList, "Banner")

        carousel.apply {
            adapter = carouselAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(10))

        carousel.setPageTransformer(compositePageTransformer)

        carousel.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(slideRunnable)
                handler.postDelayed(slideRunnable, 3000)
            }
        })
    }

    private fun fetchFilteredProducts() {
        lifecycleScope.launch {
            try {
                // Fetch products with highest ratings
                val highestRatingResponse = ApiConfig.getApiService().getFilteredProducts("Tertinggi", 1, 10)
                if (highestRatingResponse.isSuccessful) {
                    highestRatingResponse.body()?.let { updateHighestRatingAdapter(it) }
                } else {
                    val errorBody = highestRatingResponse.errorBody()?.string()
                    showErrorLog("Failed to load highest rating products. Status Code: ${highestRatingResponse.code()}, Error: $errorBody")
                }

                // Fetch new products
                val newProductResponse = ApiConfig.getApiService().getFilteredProducts("Terbaru", 1, 10)
                if (newProductResponse.isSuccessful) {
                    newProductResponse.body()?.let { updateNewProductAdapter(it) }
                } else {
                    val errorBody = newProductResponse.errorBody()?.string()
                    showErrorLog("Failed to load new products. Status Code: ${newProductResponse.code()}, Error: $errorBody")
                }

                // Fetch recommendation products
                val recommendationResponse = ApiConfig.getApiService().getProducts(1, 10)
                if (recommendationResponse.isSuccessful) {
                    recommendationResponse.body()?.let { updateRecommendationAdapter(it) }
                } else {
                    val errorBody = recommendationResponse.errorBody()?.string()
                    showErrorLog("Failed to load recommended products. Status Code: ${recommendationResponse.code()}, Error: $errorBody")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showErrorLog("An error occurred: ${e.message}")
            }
        }
    }

    private fun updateHighestRatingAdapter(products: List<Product>) {
        highestRatingAdapter.updateData(products)
    }

    private fun updateNewProductAdapter(products: List<Product>) {
        newProductAdapter.updateData(products)
    }

    private fun updateRecommendationAdapter(products: List<Product>) {
        recommendationProductAdapter.updateData(products)
    }

    private fun showErrorLog(message: String) {
        Log.e("ForYouFragment", message)
    }
}
