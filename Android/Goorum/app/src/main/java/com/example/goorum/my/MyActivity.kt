package com.example.goorum.my

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.goorum.App
import com.example.goorum.R
import com.example.goorum.Utils.HttpHelper
import com.example.goorum.Utils.HttpMethod
import com.example.goorum.databinding.ActivityMyBinding
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyActivity : AppCompatActivity() {
    val TAG = "MyActivity"

    private lateinit var binding: ActivityMyBinding

    lateinit var tvEmail: TextView
    lateinit var tvNickname: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_my
        )

        tvEmail = binding.tvEmail
        tvNickname = binding.tvNickname

        updateInfo()

        binding.ivMyBack.setOnClickListener {
            onBackPressed()
        }
        binding.tvNicknameSetting.setOnClickListener {
            val intent = Intent(this, NicknameActivity::class.java)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_up,
                R.anim.stay
            )
        }
        binding.tvChangePassword.setOnClickListener {
            val intent = Intent(this, PasswordActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.stay)
        }
        binding.tvLogout.setOnClickListener {
            val dialog = LogoutFragment()
            dialog.show(supportFragmentManager, "logout")
        }

        val intent = Intent(this, NotificationActivity::class.java)
        binding.tvMyBoard.setOnClickListener {
            intent.putExtra("title", "my_activity")
            startActivity(intent)
        }
        binding.tvLikes.setOnClickListener {
            intent.putExtra("title", "likes")
            startActivity(intent)
        }
    }

    fun updateInfo() {
        val url = "/mypage"
        val data = JsonObject()

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.GET, data)

            Log.d(TAG, "response: $result")

            tvEmail.text = App.prefs.userId
            tvNickname.text = result["nick"].asString

            return@launch
        }
    }
}