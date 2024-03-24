package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity14 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView1: RecyclerView
    private lateinit var adapter: CommunityAdapter
    private lateinit var communityList: MutableList<Community>
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var allMessagesAdapter: AllMessagesAdapter
    private lateinit var allMessagesList: MutableList<allmessages>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main14)

        recyclerView = findViewById(R.id.crv)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity14, LinearLayoutManager.HORIZONTAL, false)
        communityList = mutableListOf()


        val currentUser = auth.currentUser
        val userEmail = currentUser?.email

        userEmail?.let { email ->
            fetchUserCommunities(email)
           // val emailToDisplay = "qwerty@gmail.com"
            fetchMessages(email)
        }


        recyclerView1 = findViewById(R.id.mrv)
        recyclerView1.layoutManager = LinearLayoutManager(this)
        allMessagesList = mutableListOf()
        allMessagesAdapter = AllMessagesAdapter(this, allMessagesList) { allMessages ->
            // Handle item click here
            // Example: Show a toast with the mentor's name
            val intent = Intent(this, MainActivity15::class.java).apply {
                putExtra("name", allMessages.name)
                putExtra("picuri", allMessages.picUri)
                putExtra("useremail", allMessages.emails)


            }
            startActivity(intent)


            Toast.makeText(this, "Clicked on ${allMessages.name}", Toast.LENGTH_SHORT).show()


        }
        recyclerView1.adapter = allMessagesAdapter



        userEmail?.let { email ->
            fetchUserCommunities(email)
            fetchMessages(email)
        }



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


        val button211: Button = findViewById(R.id.button211)
        button211.setOnClickListener {
            val intent = Intent(this, MainActivity21::class.java)
            startActivity(intent)
        }



    }

    private fun fetchUserCommunities(userEmail: String) {
        database.child("communities").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val communitiesUserBelongsTo = mutableListOf<Community>()
                for (communitySnapshot in snapshot.children) {
                    val membersMap = communitySnapshot.child("members").value as? Map<String, String>
                    val mentorName = communitySnapshot.child("mentorName").value as? String
                    val picUri = communitySnapshot.child("picUri").value as? String

                    val members = membersMap?.values ?: emptyList()

                    if (members.contains(userEmail)) {
                        val communityId = communitySnapshot.key ?: ""
                        val communityMembers = mutableListOf<CommunityMember>()
                        // Fetch community members data here if needed
                        communitiesUserBelongsTo.add(Community(communityId, mentorName ?: "", picUri ?: "", communityMembers))
                    }
                }
                showUserCommunities(communitiesUserBelongsTo)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun showUserCommunities(communities: List<Community>) {
        adapter = CommunityAdapter(communities) { community ->
            val intent = Intent(this@MainActivity14, MainActivity16::class.java)
            intent.putExtra("communityId", community.communityId)
            intent.putExtra("mentorname", community.mentorName)
            intent.putExtra("picuri", community.picUri)

            val currentUser = auth.currentUser
            val userEmail = currentUser?.email
            intent.putExtra("email", userEmail)



            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }


    private fun fetchMessages(userEmail: String) {
        database.child("allmessages").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allMessagesList.clear()
                for (mentorSnapshot in snapshot.children) {
                    for (messageSnapshot in mentorSnapshot.children) {
                        val userEmailFromDB = messageSnapshot.child("emails").value.toString()
                        if (userEmailFromDB == userEmail) {
                            val lastText = messageSnapshot.child("lasttext").value.toString()
                            val mentorName = mentorSnapshot.key.toString()
                            val picUri = messageSnapshot.child("picUri").value.toString()
                            val message = allmessages(userEmail, mentorName, picUri, lastText)
                            allMessagesList.add(message)
                            Log.d("FetchedMessage", "Mentor: $mentorName, Last Text: $lastText, PicUri: $picUri")
                        }
                    }
                }
                allMessagesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }






}
