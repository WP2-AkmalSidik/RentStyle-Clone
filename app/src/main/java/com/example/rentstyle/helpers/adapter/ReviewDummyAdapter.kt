package com.example.rentstyle.helpers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rentstyle.R
import com.example.rentstyle.databinding.ProductRatingItemBinding
import com.example.rentstyle.model.data.response.Review

class ReviewDummyAdapter : RecyclerView.Adapter<ReviewDummyAdapter.ViewHolder>() {

    private var reviews: List<Review> = emptyList()

    inner class ViewHolder(private val binding: ProductRatingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            with(binding) {
                tvUsername.text = review.id
                ivReviewRating.ratingScore = review.rating
                tvReviewDescription.text = review.review
                if (review.image != null) {
                    ivReviewPhoto.setImageResource(R.drawable.img_placeholder) // Replace with your actual image loading logic

                } else {
                    ivReviewPhoto.setImageResource(R.drawable.img_placeholder)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductRatingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    fun updateReviews(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}
