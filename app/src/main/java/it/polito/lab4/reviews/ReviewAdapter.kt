package it.polito.lab4.reviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.lab4.R
import it.polito.lab4.chat.Message
import it.polito.lab4.chat.MessageAdapter

class ReviewAdapter(val context: Context, val reviewList: ArrayList<Review>):
RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.review, parent, false)
        return ReviewAdapter.ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentReview = reviewList[position]
        val viewHolder = holder as ReviewAdapter.ReviewViewHolder
        if (holder.javaClass == ReviewAdapter.ReviewViewHolder::class.java) {
            if(currentReview.reviewerUser == "No reviews" && currentReview.reviewedUser == ""
                && currentReview.rating == 0.0F && currentReview.comment == "This user has no reviews yet"){
                holder.rating.visibility = View.GONE
                holder.rating.rating= currentReview.rating
                holder.reviewer.textSize = 30F
                holder.comment.textSize = 22F
            }else{
                holder.rating.visibility = View.VISIBLE
            }
            holder.reviewer.text = currentReview.reviewerUser
            holder.comment.text = currentReview.comment

        }
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    class ReviewViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val reviewer = itemView.findViewById<TextView>(R.id.reviewer)
        val comment = itemView.findViewById<TextView>(R.id.comment)
        var rating = itemView.findViewById<RatingBar>(R.id.rating)
    }
}