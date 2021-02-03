package com.example.goorum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.goorum.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    // TODO: sharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil
            .setContentView(this, R.layout.activity_login)
        binding.bLogin.setOnClickListener {
            onSignin()
        }
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.stay)
        }
    }

    fun onSignin() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val email = binding.etId.text.toString()
        val password = binding.etPassword.text.toString()

        //

        startActivity(intent)
    }
}