package com.example.myapplication1

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CommunityAdapter(private val communities: List<Community>, private val listener: (Community) -> Unit) :
    RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.communityrow, parent, false)
        return CommunityViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val community = communities[position]
        holder.bind(community)
    }

    override fun getItemCount(): Int {
        return communities.size
    }

    class CommunityViewHolder(itemView: View, private val listener: (Community) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val communityImageView: CircleImageView = itemView.findViewById(R.id.profile)

        fun bind(community: Community) {
            community.picUri?.let { uri ->
                // Load image using Picasso or any other image loading library
                Picasso.get().load(uri).placeholder(R.drawable.profileicon).into(communityImageView)
            }

            itemView.setOnClickListener { listener(community) }
        }
    }
}


data class CommunityMember(
    val memberId: String,
    val memberName: String,
    val profilePicUri: String
)

data class Community(
    val communityId: String,
    val mentorName: String,
    val picUri: String,
    val communityMembers: List<CommunityMember>
)