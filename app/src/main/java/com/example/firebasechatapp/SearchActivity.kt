package com.example.firebasechatapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.adapters.SearchResultAdapter
import com.example.firebasechatapp.models.UserDetail
import com.example.firebasechatapp.utils.FirebaseUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SearchActivity: AppCompatActivity() {
    private var adapter: SearchResultAdapter? = null

    companion object{
        fun getIntent(context: Context):Intent{
            return Intent(context, SearchActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initViews()
    }

    private fun initViews() {
        val etSearch = findViewById<EditText>(R.id.etUsername)
        val rvItems = findViewById<RecyclerView>(R.id.rvItems)
        val ivSearch = findViewById<ImageView>(R.id.ivSearch)
        etSearch.requestFocus()
        ivSearch.setOnClickListener{
            if(etSearch.text.toString().isEmpty() || etSearch.text.toString().length < 3){
                etSearch.error = "Invalid Username"
                return@setOnClickListener
            }
            setupRecyclerView(etSearch.text.toString())
        }
    }

    private fun setupRecyclerView(queryString: String) {
        val rvItems = findViewById<RecyclerView>(R.id.rvItems)
        val collections = FirebaseUtil.getAllUserCollections()
        val query = FirebaseUtil.getAllUserCollections().whereGreaterThanOrEqualTo("username", queryString)
        val options = FirestoreRecyclerOptions.Builder<UserDetail>().setQuery(query, UserDetail::class.java).build()
        adapter = SearchResultAdapter(options, this)
        rvItems.adapter = adapter
        rvItems.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter?.startListening()

    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onResume() {
        super.onResume()
        adapter?.startListening()
    }

}