package com.example.myapplication1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class MainActivity23 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookedSessionsAdapter: Adapter2
    private lateinit var bookedSessionsList: MutableList<bookedsessions>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main23)

        recyclerView = findViewById(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        bookedSessionsList = mutableListOf()
        bookedSessionsAdapter = Adapter2(bookedSessionsList)
        recyclerView.adapter = bookedSessionsAdapter

        // Load booked sessions data
        loadBookedSessions()

    }

    private fun loadBookedSessions() {

        val queue = Volley.newRequestQueue(this)

        val url="http://192.168.0.102/smd/getbooking.php"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val bookings = mutableListOf<bookedsessions>()
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val booking = bookedsessions(
                        useremail = jsonObject.getString("useremail"),
                        mentorname = jsonObject.getString("mentorname"),
                        mentordesignation = jsonObject.getString("mentordesignation"),
                        datetime = jsonObject.getString("datetime"),
                        picuri = jsonObject.getString("picuri")
                    )
                    bookedSessionsList.add(booking)

                }
                bookedSessionsAdapter.notifyDataSetChanged()
            },
            { error ->
                Log.e("MainActivity", "Error: ${error.toString()}")
            })

        queue.add(jsonArrayRequest)
        }
    }




