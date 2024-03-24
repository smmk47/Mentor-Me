package com.example.myapplication1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

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

    }

    private fun loadBookedSessions() {
        val currentUserEmail = Firebase.auth.currentUser?.email

        if (currentUserEmail != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("bookedSessions")
            val query: Query = databaseReference.orderByChild("useremail").equalTo(currentUserEmail)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val session = snapshot.getValue(bookedsessions::class.java)
                        session?.let {
                            bookedSessionsList.add(it)
                        }
                    }
                    bookedSessionsAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

}