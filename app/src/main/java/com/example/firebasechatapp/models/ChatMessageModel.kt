package com.example.firebasechatapp.models

data class ChatMessageModel(
    var message: String,
    var senderId: String,
    var timestamp: java.util.Date = java.util.Date()
)