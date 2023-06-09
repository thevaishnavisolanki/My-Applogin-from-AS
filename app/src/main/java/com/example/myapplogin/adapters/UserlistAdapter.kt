package com.example.myapplogin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplogin.ChatActivity
import com.example.myapplogin.R
import com.example.myapplogin.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserlistAdapter (
    private var mContext: Context,
    private var mUser: List<User>,

    ) : RecyclerView.Adapter<UserlistAdapter.ViewHolder>() {

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserlistAdapter.ViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.item_user_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserlistAdapter.ViewHolder, position: Int) {

        val user = mUser[position]
        holder.chatusernameTextView.text = user.username
        holder.userbioTextView.text = user.bio
        Picasso.get().load(user.image).placeholder(R.drawable.ic_avatar).into(holder.userchatprofileImage)
        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            holder.itemView.setOnClickListener{
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("selectedUser", user)
                context.startActivity(intent)
            }
        }

    }

    inner class ViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chatusernameTextView: TextView = itemView.findViewById(R.id.userName)
        var userbioTextView: TextView = itemView.findViewById(R.id.Chatbio)
        var userchatprofileImage: CircleImageView = itemView.findViewById(R.id.chat_profile_image)

    }
}
