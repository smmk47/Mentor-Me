import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication1.R
import com.example.myapplication1.searchresult

class Adapter4(private val context: Context) :
    RecyclerView.Adapter<Adapter4.ViewHolder>() {

    private val searchResults = mutableListOf<searchresult>()

    fun submitList(newList: List<searchresult>) {
        searchResults.clear()
        searchResults.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.searchrow, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = searchResults[position]

        // Populate your views with data from searchresult object
        holder.mentorNameTextView.text = result.mentorname
       // holder.userEmailTextView.text = result.useremail
        holder.statusTextView.text = result.status
        holder.sessionPriceTextView.text = result.sessionprice
        holder.designationTextView.text = result.designation

        Glide.with(context)
            .load(result.picuri) // Assuming picuri is the URL of the image
            .placeholder(R.drawable.profileicon) // Placeholder image while loading
            .error(R.drawable.rectangle15) // Error image if loading fails
            .into(holder.profileImageView)
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView1)
        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val mentorNameTextView: TextView = itemView.findViewById(R.id.textViewname)
       // val userEmailTextView: TextView = itemView.findViewById(R.id.textViewpicuri)
        val statusTextView: TextView = itemView.findViewById(R.id.textViewstatus)
        val sessionPriceTextView: TextView = itemView.findViewById(R.id.textViewsessiondatetime)
        val designationTextView: TextView = itemView.findViewById(R.id.textViewdesignaton)
    }
}
