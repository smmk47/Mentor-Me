package com.example.myapplication1


import java.io.Serializable

data class message(
    val userId: String = "",
    val messageText: String = "",
    val timestamp: Long = 0,
    val audioUrl: String? = null,
    val imageUrl: String? = null,
    val fileUrl: String? = null,
    val videoUrl: String? = null,
    var key: String? = "",
    var recivername: String? = ""


) : Serializable
