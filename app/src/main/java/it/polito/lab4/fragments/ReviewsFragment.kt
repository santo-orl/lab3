package it.polito.lab4.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import it.polito.lab4.R
import it.polito.lab4.reviews.Review
import it.polito.lab4.ViewModel
import it.polito.lab4.reviews.ReviewAdapter
import it.polito.lab4.skills.Skill
import it.polito.lab4.timeSlots.Adapter_homeFrg
import it.polito.lab4.timeSlots.Slot
import kotlinx.android.synthetic.main.fragment_time_slot_list.*


class ReviewsFragment : Fragment() {
    private val vm: ViewModel by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private var id = ""

    private lateinit var slot: Slot
    private lateinit var reviewRecView: RecyclerView
    private lateinit var adapterReview: ReviewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setTitle("Reviews")

        reviewRecView = view.findViewById(R.id.recViewReviews)

        vm.email.observe(this.viewLifecycleOwner) {
            id = it
        }

        vm.slot.observe(this.viewLifecycleOwner) {
            slot = it
            Log.i("ALLORA", slot.toString())
            if (slot.user!= ""){
                readReviews(slot.user)
            }else{
                readReviews(id)

            }
        }
    }

    private fun readReviews(user: String){

        db.collection("users").document(user).collection("reviews").get()
            .addOnSuccessListener{
                    result->
                var reviewList = arrayListOf<Review>()
                for (doc in result.documents){

                    Log.i("rating all", doc.data.toString())
                    val s = doc.data as HashMap<*, *>
                    val r = Review(
                        s["reviewerUser"].toString(),
                        s["reviewedUser"].toString(),
                        s["rating"].toString().toFloat(),
                        s["comment"].toString()
                    )

                    reviewList.add(r)

                }

                if(reviewList.isEmpty()){
                    reviewList.add(
                        Review(
                            "No reviews",
                            "",
                            0.0F,
                            "This user has no reviews yet"
                        )
                    )
                }

                reviewRecView.layoutManager = LinearLayoutManager(this.activity)
                adapterReview = ReviewAdapter(requireContext(),reviewList)
                reviewRecView.adapter = adapterReview

            }.addOnFailureListener { e->
                Log.i("No reviews", e.toString())
            }


    }


}