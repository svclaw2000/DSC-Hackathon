package com.example.goorum

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.goorum.data.Signin
import com.example.goorum.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    // TODO: sharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil
            .setContentView(this, R.layout.activity_login)

        etEmail = binding.etId.editText!!
        etPassword = binding.etPassword.editText!!

        binding.bLogin.setOnClickListener {
            if (etEmail.text.toString() != "" && etPassword.text.toString() != "") {
                onSignin()
            }
        }
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.stay)
        }
    }

    fun onSignin() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        val signin = Signin(email, password)

        if (signin.matchesExistingAccount()) {
            App.prefs.userId = email
            App.prefs.userPw = password
            startActivity(intent)
        } else {
            val dialog = LoginFailureFragment()
            dialog.show(supportFragmentManager, "login failed")
        }
    }
}