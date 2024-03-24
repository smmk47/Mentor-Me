package com.example.myapplication1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AllMessagesAdapter(
    private val context: Context,
    private val allMessagesList: List<allmessages>,
    private val onItemClick: (allmessages) -> Unit
) : RecyclerView.Adapter<AllMessagesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mentorNameTextView: TextView = itemView.findViewById(R.id.textViewmentorname)
        val lastMessageTextView: TextView = itemView.findViewById(R.id.lasttext)
        val imageView: ImageView = itemView.findViewById(R.id.textView12)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(allMessagesList[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.messagerow, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val allMessages = allMessagesList[position]
        holder.mentorNameTextView.text = allMessages.name
        holder.lastMessageTextView.text = allMessages.lasttext
        // Load image using any image loading library like Picasso, Glide, Coil, etc.
        // For example, using Glide:
        Glide.with(context)
            .load(allMessages.picUri)
            .placeholder(R.drawable.profileicon) // Placeholder image while loading
            .error(R.drawable.profileicon) // Error image if loading fails
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return allMessagesList.size
    }
}
