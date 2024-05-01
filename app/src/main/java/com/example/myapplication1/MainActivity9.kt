package com.example.myapplication1

import Adapter4
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class MainActivity9 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main9)

        recyclerView = findViewById(R.id.srv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter4(this)
        recyclerView.adapter = adapter

        val searchText = intent.getStringExtra("searchText")

        val mentorsList = mutableListOf<searchresult>()

        // Initialize Volley request queue
        val requestQueue = Volley.newRequestQueue(this)

        val url = "http://192.168.0.102/smd/searchmentors.php?search=$searchText"

        // Create JSON array request
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                mentorsList.clear() // Clear previous search results
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val searchResult = searchresult(
                        jsonObject.getString("name"),
                        "mentor1@gmail.com",
                        jsonObject.getString("status"),
                        jsonObject.getString("sessionprice"),
                        jsonObject.getString("designation"),
                        jsonObject.getString("imageurl")
                    )
                    mentorsList.add(searchResult)
                }
                adapter.submitList(mentorsList)
            },
            { error ->
                // Handle error
            })

        // Add JSON array request to the request queue
        requestQueue.add(jsonArrayRequest)

        val button17: Button = findViewById(R.id.button17)
        button17.setOnClickListener {
            val intent = Intent(this, homepage::class.java)
            startActivity(intent)
        }

        val button18: Button = findViewById(R.id.button18)
        button18.setOnClickListener {
            val intent = Intent(this, searchhome::class.java)
            startActivity(intent)
        }

        val button22: Button = findViewById(R.id.button22)
        button22.setOnClickListener {
            val intent = Intent(this, MainActivity12::class.java)
            startActivity(intent)
        }

        val button20: Button = findViewById(R.id.button20)
        button20.setOnClickListener {
            val intent = Intent(this, MainActivity14::class.java)
            startActivity(intent)
        }

        val button211: Button = findViewById(R.id.button21)
        button211.setOnClickListener {
            val intent = Intent(this, MainActivity21::class.java)
            startActivity(intent)
        }
    }
}
