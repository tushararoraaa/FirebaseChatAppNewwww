package com.example.firebasechatapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechatapp.utils.FirebaseUtil
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginOtpActivity: AppCompatActivity() {
    private var phone = ""
    private var verifyId = ""
    private val auth = FirebaseAuth.getInstance()

    companion object{
        fun getIntent(context: Context, phone: String): Intent{
            val intent = Intent(context, LoginOtpActivity::class.java)
            intent.putExtra("phone", phone)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_otp)
        initViews()
    }

    private fun initViews() {
        if(intent != null && intent.extras != null){
            phone = intent.extras!!.getString("phone", "")
            sendOtp(phone)
            val btnOtp = findViewById<Button>(R.id.btnEnterOtp)
            val etOtp = findViewById<EditText>(R.id.etOtp)
            btnOtp.setOnClickListener{
                val enteredOtp = etOtp.text.toString()
                val credential = PhoneAuthProvider.getCredential(verifyId!!, enteredOtp)
                signInWithPhoneAuthCredential(credential)
            }
        }
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
////                Log.w(TAG, "onVerificationFailed", e)
//
//                if (e is FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                } else if (e is FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
//                    // reCAPTCHA verification attempted with null Activity
//                }
            Toast.makeText(baseContext, "Something Went Wrong", Toast.LENGTH_SHORT).show()

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            verifyId = verificationId
            Toast.makeText(baseContext, "OTP SENT", Toast.LENGTH_SHORT).show()

        }
    }

    private fun sendOtp(phone: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


        private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(baseContext, "Succesfully signed in using OTP", Toast.LENGTH_SHORT).show()
                        val intent = UsernameActivity.getIntent(this, phone)
                        startActivity(intent)
                    } else {
                        // Sign in failed, display a message and update the UI
                        Toast.makeText(baseContext, "Wrong OTP", Toast.LENGTH_SHORT).show()

                        // Update UI
                    }
                }
        }

}