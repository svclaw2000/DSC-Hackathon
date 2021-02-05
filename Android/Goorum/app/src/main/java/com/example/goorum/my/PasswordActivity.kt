package com.example.goorum.my

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.goorum.R
import com.example.goorum.databinding.ActivityPasswordBinding
import com.example.goorum.utils.HttpHelper
import com.example.goorum.utils.HttpMethod
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordBinding
    lateinit var etCurPassword:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password)

        val etNewPassword = binding.etNewPassword.editText!!
        val etConfirmNewPassword = binding.etConfirmNewPassword.editText!!
        etCurPassword = binding.etCurPassword.editText!!

        etConfirmNewPassword.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (etNewPassword.text.toString() != etConfirmNewPassword.text.toString()) {
                    etConfirmNewPassword.error = "비밀번호가 일치하지 않습니다"
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.bChangePassword.setOnClickListener {
            comparePassword(etCurPassword.text.toString(), etNewPassword.text.toString())
        }

        binding.ivClose.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.stay, R.anim.slide_down)
    }

    fun comparePassword(curPwd: String, newPwd: String) {
        val url = "/member/changePWD"
        val data = JsonObject()

        data.addProperty("pwd", curPwd)
        data.addProperty("newPwd", newPwd)

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.GET, data)

            if (result["result"].asInt == 1) {
                onBackPressed()
            } else {
                val dialog = PasswordChangeFailureFragment()
                dialog.show(supportFragmentManager, "login failed")
            }
        }
    }
}