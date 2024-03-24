package com.example.myapplication1

import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper {
    private val database = FirebaseDatabase.getInstance()
    private val notificationsRef = database.getReference("notifications")

    fun addNotification(notification: notifications) {
        val notificationId = notificationsRef.push().key
        notificationId?.let {
            notificationsRef.child(it).setValue(notification)
                .addOnSuccessListener {
                    // Notification added successfully
                    println("Notification added successfully")
                }
                .addOnFailureListener { e ->
                    // Handle error
                    println("Error adding notification: ${e.message}")
                }
        }
    }
}
