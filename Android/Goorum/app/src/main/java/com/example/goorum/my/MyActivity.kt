package com.example.goorum.my

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.goorum.R
import com.example.goorum.databinding.ActivityMyBinding

class MyActivity : AppCompatActivity() {
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
            // TODO: sharedPreference
        }
    }

    fun updateInfo() {
        // TODO: Member instance 생성 후 사용
        tvEmail.text = "khs990419@gmail.com"
        tvNickname.text = "번둥천개"
    }
}