package com.example.myapplication1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adapter3(private val reviews: List<reviews>) :
    RecyclerView.Adapter<Adapter3.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mentorNameTextView: TextView = itemView.findViewById(R.id.mentorNameTextView)
        val feedbackTextView: TextView = itemView.findViewById(R.id.feedbackTextView)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rowreview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[position]
        holder.mentorNameTextView.text = review.mentorname
        holder.feedbackTextView.text = review.feedback
        holder.ratingBar.rating = review.rating.toFloat() // Assuming rating is a String and needs to be converted to Float
    }

    override fun getItemCount(): Int {
        return reviews.size
    }
}


