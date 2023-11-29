package com.example.firebasechatapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtil {

    companion object{
        fun getCurrentUserId(): String{
            return if(FirebaseAuth.getInstance() != null && FirebaseAuth.getInstance().uid != null){
                FirebaseAuth.getInstance().uid!!
            }else{
                ""
            }

        }

        fun isUserLoggedIn(): Boolean{
            return getCurrentUserId().isNotEmpty()
        }

        fun getUserDetails(): DocumentReference{
            return FirebaseFirestore.getInstance().collection("users").document(getCurrentUserId())
        }

        fun getAllUserCollections() : CollectionReference{
            return FirebaseFirestore.getInstance().collection("users")
        }

        fun getChatRoom(chatRoomId: String): DocumentReference{ // ye ek outer level p chat room bna degi
            return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId)
        }

        fun getChatRoomId(userid1: String, userid2: String): String{
            return if(userid1.hashCode() < userid2.hashCode()){
                userid1 + "_" + userid2
            }else{
                userid2 + "_" + userid1
            }
        }

        fun getChatsReferenceCollection(chatRoomId: String): CollectionReference{   // ye chatroom m ander ek chats ki collection bna degi on basis of chatroomid
            return getChatRoom(chatRoomId).collection("chats")
        }
    }


}