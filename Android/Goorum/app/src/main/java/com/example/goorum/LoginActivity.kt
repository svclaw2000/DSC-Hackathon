package com.example.goorum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.goorum.Data.Member
import com.example.goorum.databinding.ActivityLoginBinding
import com.example.goorum.Utils.HttpHelper
import com.example.goorum.Utils.HttpMethod
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    val TAG = "LoginActivity"

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
            Member.login()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            return@setOnClickListener
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email != "" && password != "") {
                val signin = Signin(email, password)
                signin.matchesExistingAccount()
            }
        }
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.stay)
        }
    }

    fun onSignin() {
        val intent = Intent(this, MainActivity::class.java)
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        App.prefs.userId = email
        App.prefs.userPw = password
        startActivity(intent)
    }

    fun onFailed() {
        val dialog = LoginFailureFragment()
        dialog.show(supportFragmentManager, "login failed")
    }

    inner class Signin(val email: String = "test", val password: String = "test") {
        val TAG = "Signin"

        fun matchesExistingAccount() {
            val url = "/member/login"

            val data = JsonObject()
            data.addProperty("id", email)
            data.addProperty("pwd", password)

            GlobalScope.launch(Dispatchers.Main) {
                val result = HttpHelper.request(url, HttpMethod.POST, data)
                Log.d(TAG, result.toString())
                if (result["result"].asInt == 1) {
                    onSignin()
                } else {
                    onFailed()
                }
            }
        }
    }
}