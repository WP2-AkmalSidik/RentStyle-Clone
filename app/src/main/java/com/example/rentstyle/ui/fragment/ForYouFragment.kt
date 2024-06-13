package com.example.rentstyle.ui.fragment

import ProductAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForYouFragment : Fragment() {
    private var _binding: FragmentForYouBinding? = null
    private val binding get() = _binding!!

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
        carousel.currentItem = if (carousel.currentItem == 2) 0 else carousel.currentItem + 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForYouBinding.inflate(inflater, container, false)

        createCarouselInstance()
        createProductRecyclerViewInstance()
        fetchData()
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

        val imageList = arrayListOf(R.drawable.img_placeholder, R.drawable.img_placeholder, R.drawable.img_placeholder)
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

    private fun fetchData() {
        // Menggunakan coroutine untuk memanggil suspend function
        lifecycleScope.launch {
            try {
                val products = withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getProducts(page = 1, size = 100) // Parameter pagination
                }
                updateAdapters(products)
            } catch (e: Exception) {
                // Tangani kesalahan
                e.printStackTrace()
            }
        }
    }

    private fun updateAdapters(products: List<Product>) {
        val highestRatedProducts = products.sortedByDescending { it.avgRating }.take(10)
        val newProducts = products.sortedByDescending { it.id }.take(10) // Assuming 'id' increments with new products

        highestRatingAdapter.updateData(highestRatedProducts)
        newProductAdapter.updateData(newProducts)
        recommendationProductAdapter.updateData(products) // Assuming recommendation based on all products
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(slideRunnable)
        _binding = null
    }
}
