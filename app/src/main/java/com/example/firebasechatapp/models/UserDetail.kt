package com.example.firebasechatapp.models

import java.sql.Timestamp


data class UserDetail(
    var username: String = "",
    var phone: String = "",
    var timestamp: java.util.Date = java.util.Date(),
    var userId: String = ""
)
