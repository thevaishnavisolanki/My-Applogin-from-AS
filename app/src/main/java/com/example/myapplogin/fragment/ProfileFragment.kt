package com.example.myapplogin.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplogin.AccountSettingsActivity
import com.example.myapplogin.R
import com.example.myapplogin.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    private lateinit var profileID: String
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var button: Button
    private lateinit var textViewFullName: TextView
    private lateinit var textViewUsername: TextView
    private lateinit var textViewBio: TextView
    private lateinit var imageView: ImageView
    private lateinit var textViewFollowers: TextView
    private lateinit var textviewFollowing: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        button = view.findViewById(R.id.edit_account_settings_btn)
        imageView = view.findViewById(R.id.pro_image_profile_frag)
        textViewUsername = view.findViewById(R.id.profile_fragment_username)
        textViewFullName = view.findViewById(R.id.full_name_profile_frag)
        textViewBio = view.findViewById(R.id.bio_profile_frag)
        textViewFollowers = view.findViewById(R.id.total_followers)
        textviewFollowing = view.findViewById(R.id.total_following)


        if (FirebaseAuth.getInstance().currentUser != null) {
            firebaseUser = FirebaseAuth.getInstance().currentUser!!
        }
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null) {
            this.profileID = pref.getString("profileID", "none").toString()
        }
        if(this::firebaseUser.isInitialized){
        if (profileID == firebaseUser.uid) {

            button.text = "Edit profile"
        } else if (profileID != firebaseUser.uid) {
            checkfollowandfollowingButtonStatus()
        }
        }

        button.setOnClickListener {
            val intent = Intent(context, AccountSettingsActivity::class.java)
            startActivity(intent)
        }




        getFollowers()
        getFollowing()
        userInfo()


        return view
    }

    companion object;

    private fun checkfollowandfollowingButtonStatus() {
        val followingRef = firebaseUser.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1)
                .child("Following")
        }
        followingRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshort: DataSnapshot) {
                if (snapshort.child(profileID).exists()) {
                    button.text = "Following"
                } else {
                    button.text = "Follow"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

    }

    private fun getFollowers() {
        val followersRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(profileID)
            .child("Followers")

        followersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    textViewFollowers.text = snapshot.childrenCount.toString()
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }


    private fun getFollowing() {
        val followingRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(profileID)
            .child("Following")


        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    textviewFollowing.text = snapshot.childrenCount.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun userInfo() {
        val usersRef = FirebaseDatabase.getInstance().reference.child("users").child(profileID)
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if(!user!!.image.isNullOrBlank()){
                        Picasso.get().load(user.image).placeholder(R.drawable.ic_avatar)
                            .into(imageView)
                    }
                    textViewUsername.text = user.username
                    textViewFullName.text = user.name
                    textViewBio.text = user.bio

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


    override fun onStop() {
        super.onStop()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileID", firebaseUser.uid)
        pref?.apply()
    }

    override fun onPause() {
        super.onPause()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileID", firebaseUser.uid)
        pref?.apply()

    }

    override fun onDestroy() {
        super.onDestroy()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileID", firebaseUser.uid)
        pref?.apply()

    }


}

