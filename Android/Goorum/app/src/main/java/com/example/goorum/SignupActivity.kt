package com.example.goorum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.goorum.databinding.ActivityLoginBinding
import com.example.goorum.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    val TAG = "SignupActivity"

    lateinit var binding: ActivitySignupBinding
    lateinit var email: String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        binding.ivClose.setOnClickListener {
            onBackPressed()
        }

        binding.bRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)

            onSubmit()

            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_down)
    }

    fun onSubmit() {
        email = binding.etSignup.text.toString()
        password = binding.etCreatePw.text.toString()
        Log.d(TAG, "email: $email / password: $password")

        // TODO: server로 전달
    }
}