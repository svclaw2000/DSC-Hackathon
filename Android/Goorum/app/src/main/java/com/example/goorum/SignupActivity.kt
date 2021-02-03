package com.example.goorum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
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
            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

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