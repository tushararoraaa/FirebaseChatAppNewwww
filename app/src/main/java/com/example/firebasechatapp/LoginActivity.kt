package com.example.firebasechatapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryCodePicker

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }

    private fun initView() {
        val btnOtp = findViewById<Button>(R.id.btnOtp)
        val ccp = findViewById<CountryCodePicker>(R.id.ccp)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        ccp.registerCarrierNumberEditText(etPhone)
        btnOtp.setOnClickListener{
            if(!ccp.isValidFullNumber){
                etPhone.error = "Enter valid number"
                Toast.makeText(baseContext, "Enter valid number", Toast.LENGTH_SHORT).show()
            }else{
                val intent = LoginOtpActivity.Companion.getIntent(baseContext, ccp.fullNumberWithPlus)
                startActivity(intent)
            }
        }
    }
}