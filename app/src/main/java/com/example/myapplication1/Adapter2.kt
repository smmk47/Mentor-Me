package com.example.myapplication1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class Adapter2(private val bookedSessionsList: List<bookedsessions>) :
    RecyclerView.Adapter<Adapter2.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val datetimeTextView: TextView = itemView.findViewById(R.id.textViewsessiondatetime)
        val mentorNameTextView: TextView = itemView.findViewById(R.id.textViewname)
        val mentorDesignationTextView: TextView = itemView.findViewById(R.id.textViewdesignaton)
        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView) // Assuming ImageView ID is profileImageView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rowbooked, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = bookedSessionsList[position]
        holder.datetimeTextView.text = session.datetime
        holder.mentorNameTextView.text = session.mentorname
        holder.mentorDesignationTextView.text = session.mentordesignation

        // Load and display the mentor's profile picture using Glide
        Glide.with(holder.itemView.context)
            .load(session.picuri) // Assuming picuri is the URL of the image
            .placeholder(R.drawable.profileicon) // Placeholder image while loading
            .error(R.drawable.rectangle15) // Error image if loading fails
            .into(holder.profileImageView)
    }


    override fun getItemCount(): Int {
        return bookedSessionsList.size
    }
}




