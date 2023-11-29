package com.example.firebasechatapp.models

import java.sql.Timestamp

data class ChatRoomModel(
    var chatRoomId: String = "",
    var userIds: ArrayList<String> = arrayListOf(),
    var timestamp: java.util.Date = java.util.Date(),
    var lastMessageSenderId: String = ""
)