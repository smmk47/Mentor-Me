package com.example.myapplication1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainActivity24 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var recyclerView: RecyclerView
        lateinit var notificationAdapter: NotificationAdapter
        lateinit var notificationsList: MutableList<notifications>
        lateinit var databaseReference: DatabaseReference

        val database: DatabaseReference = FirebaseDatabase.getInstance().reference
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main24)



        recyclerView = findViewById(R.id.nrv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        notificationsList = mutableListOf()
        notificationAdapter = NotificationAdapter(notificationsList)
        recyclerView.adapter = notificationAdapter

        val currentUser = auth.currentUser
        val userEmail = currentUser?.email

        val currentUserEmail = userEmail.toString()   // Set your current user's email here
        databaseReference = FirebaseDatabase.getInstance().getReference("notifications")
        val query: Query = databaseReference.orderByChild("useremail").equalTo(currentUserEmail)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                notificationsList.clear()
                for (snapshot in dataSnapshot.children) {
                    val notification = snapshot.getValue(notifications::class.java)
                    notification?.let {
                        notificationsList.add(it)
                    }
                }
                notificationAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled
            }
        })

    }
}