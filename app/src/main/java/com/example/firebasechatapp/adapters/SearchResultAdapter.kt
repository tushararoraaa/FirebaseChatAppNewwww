package com.example.firebasechatapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.ChatActivity
import com.example.firebasechatapp.R
import com.example.firebasechatapp.models.UserDetail
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SearchResultAdapter(options: FirestoreRecyclerOptions<UserDetail>, context: Context): FirestoreRecyclerAdapter<UserDetail, SearchResultAdapter.RecentSearchViewHolder>(
options) {

    override fun onBindViewHolder(
        holder: RecentSearchViewHolder,
        position: Int,
        model: UserDetail
    ) {
        if(holder is RecentSearchViewHolder){
            holder.bindView(model, holder.itemView.context)
        }
    }

    inner class RecentSearchViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvUsername = view.findViewById<TextView>(R.id.tvUserName)
        val tvPhone = view.findViewById<TextView>(R.id.tvPhone)
        val cvMain = view.findViewById<CardView>(R.id.cvMain)
        fun bindView(data: UserDetail, context: Context) {
            tvUsername.text = data.username
            tvPhone.text = data.phone
            cvMain.setOnClickListener{
                val intent = ChatActivity.Companion.getIntent(context, data.username, data.phone, data.userId)
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_search, parent, false)
        return RecentSearchViewHolder(view)
    }
}