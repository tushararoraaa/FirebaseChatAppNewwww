package com.example.firebasechatapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechatapp.models.UserDetail
import com.example.firebasechatapp.utils.FirebaseUtil
import com.google.firebase.firestore.toObject
import java.sql.Timestamp

class UsernameActivity: AppCompatActivity() {
    private var phone = ""
    private var userModel: UserDetail? = null

    companion object{
        fun getIntent(context: Context, phone: String): Intent {
            val intent = Intent(context, UsernameActivity::class.java)
            intent.putExtra("phone", phone)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)
        initViews()
    }

    private fun initViews() {
        if(intent != null && intent.extras != null){
            phone = intent.extras!!.getString("phone", "")
            val btnEnterUsername = findViewById<Button>(R.id.btnEnterUsername)
            val etUsername = findViewById<EditText>(R.id.etUsername)
            getUsername()
            btnEnterUsername.setOnClickListener{
                setUserName(etUsername)
            }
            }
        }

    private fun setUserName(editText: EditText) {
        if(editText.text.toString().length < 3){
            editText.error = "Username must be  greater than 3"
            return
        }
        if(userModel != null){
            userModel?.username = editText.text.toString()
        }else{
            val currentTimestampMillis = System.currentTimeMillis()
            val timestamp = Timestamp(currentTimestampMillis)
            userModel = UserDetail(editText.text.toString(), phone, timestamp, FirebaseUtil.getCurrentUserId())
        }

        FirebaseUtil.getUserDetails().set(userModel!!).addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {
                val intent = Intent(this@UsernameActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
    }

    private fun getUsername(){
        FirebaseUtil.getUserDetails().get().addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {
                userModel = p0.result.toObject<UserDetail>()
            }
        }
    }
}