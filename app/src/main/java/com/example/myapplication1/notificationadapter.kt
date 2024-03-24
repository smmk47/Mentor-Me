// NotificationAdapter.kt
package com.example.myapplication1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class NotificationAdapter(private val notifications: MutableList<notifications>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {




    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      //  val textViewUserEmail: TextView = itemView.findViewById(R.id.textViewUserEmail)
        val textViewNotificationData: TextView = itemView.findViewById(R.id.textView33)
        val clearButton: ImageButton = itemView.findViewById(R.id.clearnotification)

        init {
            clearButton.setOnClickListener {
                val position = adapterPosition
                val notification = notifications[position]

                // Sanitize the email address to remove illegal characters from the database path
                val sanitizedEmail = notification.useremail.replace('.', '_')

                // Remove notification from Firebase Realtime Database
                FirebaseDatabase.getInstance().getReference("notifications")
                    .child(sanitizedEmail) // Use sanitized email as part of the path
                    .removeValue()
                    .addOnSuccessListener {
                        // Remove notification from local list
                        notifications.removeAt(position)
                        notifyItemRemoved(position)
                    }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notificationrow, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]
       // holder.textViewUserEmail.text = "User Email: ${notification.useremail}"
        holder.textViewNotificationData.text = notification.notificationdata
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}

