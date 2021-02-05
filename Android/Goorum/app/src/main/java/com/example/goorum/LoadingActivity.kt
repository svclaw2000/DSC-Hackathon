package com.example.goorum

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.example.goorum.my.MyActivity
import com.example.goorum.utils.HttpHelper
import com.example.goorum.utils.HttpMethod
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoadingActivity : AppCompatActivity() {
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        imageView = findViewById(R.id.imageView)
        splashAnimation()
        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()

        // 자동 로그인
        // TODO: loading page
//        val curId = App.prefs.userId
//        val curPw = App.prefs.userPw
//        if (curId != null && curPw != null) {
//            matchesExistingAccount(curId, curPw)
//        }

        handler.postDelayed(Runnable {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }, 2000)
    }

    fun onSignin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onFailed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
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
            } else {
                onFailed()
            }
        }
    }

    @UiThread
    private fun splashAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.splash)
        imageView.startAnimation(animation)
    }
}