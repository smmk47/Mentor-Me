package com.example.myapplication1




import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter1(private val mentorsList: List<newmentor>, private val context: Context) :
    RecyclerView.Adapter<Adapter1.MyViewHolder>() {

    // Interface for item click listener
    interface OnItemClickListener {
        fun onItemClick(mentor: newmentor)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.textViewname)
        var designation = itemView.findViewById<TextView>(R.id.textViewdesignaton)
        var sessionprice = itemView.findViewById<TextView>(R.id.textViewsessionprice)
        var status = itemView.findViewById<TextView>(R.id.textViewStatus)
        var picuri = itemView.findViewById<TextView>(R.id.textViewpicuri)

        init {
            // Set click listener for each item
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(mentorsList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.rowh1, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mentorsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mentor = mentorsList[position]
        holder.name.text = mentor.name
        holder.designation.text = mentor.designation
        holder.status.text = mentor.status
        holder.sessionprice.text = mentor.price
        holder.picuri.text = mentor.picuri.toString()

    }
}
