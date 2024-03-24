package com.example.myapplication1
import Adapter4
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity9 : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main9)



        val searchText = intent.getStringExtra("searchText")

        recyclerView = findViewById(R.id.srv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter4(this)
        recyclerView.adapter = adapter

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().reference.child("mentors")

        // Construct individual queries for each field
        val queryByName: Query = databaseReference.orderByChild("name").startAt(searchText)
            .endAt(searchText + "\uf8ff")
        val queryByDesignation: Query = databaseReference.orderByChild("designation").startAt(searchText)
            .endAt(searchText + "\uf8ff")
        val queryBySessionPrice: Query = databaseReference.orderByChild("sessionPrice").startAt(searchText)
            .endAt(searchText + "\uf8ff")

        // List to hold search results
        val mentorsList = mutableListOf<searchresult>()

        // Add listener for each query
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val mentor = snapshot.getValue(newmentor::class.java)
                    mentor?.let {
                        val searchResult = searchresult(
                            mentor.name,
                            mentor.price,
                            mentor.status,
                            mentor.description,
                            mentor.designation,
                            mentor.picuri
                        )
                        mentorsList.add(searchResult)
                    }
                }
                adapter.submitList(mentorsList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }

        // Add listeners to each query
        queryByName.addValueEventListener(listener)
        queryByDesignation.addValueEventListener(listener)
        queryBySessionPrice.addValueEventListener(listener)



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
