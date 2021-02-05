package com.example.goorum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.goorum.databinding.ActivityLoginBinding
import com.example.goorum.utils.HttpHelper
import com.example.goorum.utils.HttpMethod
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    val TAG = "LoginActivity"

    lateinit var binding: ActivityLoginBinding
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil
            .setContentView(this, R.layout.activity_login)

        // 자동 로그인
        // TODO: loading page
        val curId = App.prefs.userId
        val curPw = App.prefs.userPw

        if (curId == null) {
            Log.i(TAG, "현재 id: ${curId}는 null")
        } else if (curId == "") {
            Log.i(TAG, "현재 id: ${curId}는 빈 문자열")
        } else {
            Log.i(TAG, "현재 id: ${curId}는 둘다 아님...")
        }

        if (curId != "" && curPw != "" && curId != null && curPw != null) {
            Log.i(TAG, "현재 id: $curId, 현재 pw: $curPw")
            matchesExistingAccount(curId, curPw)
        }

        etEmail = binding.etId.editText!!
        etPassword = binding.etPassword.editText!!

        binding.bLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email != "" && password != "") {
                matchesExistingAccount(email, password)
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

    fun matchesExistingAccount(email: String, password: String) {
        val TAG = "Signin"
        val url = "/member/login"

        val data = JsonObject()
        data.addProperty("id", email)
        data.addProperty("pwd", password)

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.POST, data)
            Log.d(TAG, result.toString())
            if (result["result"].asInt == 1) {
                onSignin()
                return@launch
            } else {
                onFailed()
                return@launch
            }
        }
    }
}