package com.example.firebasechatapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechatapp.models.ChatMessageModel
import com.example.firebasechatapp.models.ChatRoomModel
import com.example.firebasechatapp.utils.FirebaseUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.toObject
import java.sql.Timestamp

class ChatActivity: AppCompatActivity() {

    private var otherUsername = ""
    private var otherPhone = ""
    private var otherUserId = ""
    private var chatRoomId = ""
    private var chatRoomModel: ChatRoomModel? = null
    private var etWriteMessage: EditText? = null

    companion object{
        fun getIntent(context: Context, username: String, phone: String, userId: String): Intent{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("phone", phone)
            intent.putExtra("userId", userId)
            return intent

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        if(intent != null && intent.extras != null){
            otherUsername = intent?.extras?.getString("username", "")!!
            otherPhone = intent?.extras?.getString("phone", "")!!
            otherUserId = intent?.extras?.getString("userId", "")!!
            initViews()
        }

    }

    private fun initViews() {
        etWriteMessage = findViewById<EditText>(R.id.etWriteMessage)
        val ivSend = findViewById<ImageView>(R.id.ivSend)
        etWriteMessage?.requestFocus()
        ivSend.setOnClickListener{
            val message = etWriteMessage?.text.toString()
            if(message.isEmpty()){
                return@setOnClickListener
            }
            sendMessageToUser(message)
        }
        createOrOpenChatRoom()
    }

    private fun sendMessageToUser(message: String) {
        val currentTimestampMillis = System.currentTimeMillis()
        val timestamp = Timestamp(currentTimestampMillis)
        chatRoomModel?.lastMessageSenderId = FirebaseUtil.getCurrentUserId()
        chatRoomModel?.timestamp = timestamp
        FirebaseUtil.getChatRoom(chatRoomId).set(chatRoomModel!!)

        val chatMessageModel = ChatMessageModel(message, FirebaseUtil.getCurrentUserId(), timestamp)
        FirebaseUtil.getChatsReferenceCollection(chatRoomId).add(chatMessageModel).addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {
                etWriteMessage?.setText("")
            }
        }
    }

    private fun createOrOpenChatRoom() {
        chatRoomId = FirebaseUtil.getChatRoomId(FirebaseUtil.getCurrentUserId(), otherUserId)

        FirebaseUtil.getChatRoom(chatRoomId).get().addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {
                chatRoomModel = p0.result.toObject(ChatRoomModel::class.java)
                if(chatRoomModel == null){ //FIRST TIME CHAT
                    val list = ArrayList<String>()
                    list.add(otherUserId)
                    list.add(FirebaseUtil.getCurrentUserId())
                    val currentTimestampMillis = System.currentTimeMillis()
                    val timestamp = Timestamp(currentTimestampMillis)
                    chatRoomModel = ChatRoomModel(chatRoomId, list, timestamp, "")
                    FirebaseUtil.getChatRoom(chatRoomId).set(chatRoomModel!!)
                }
            }
        }

    }

}