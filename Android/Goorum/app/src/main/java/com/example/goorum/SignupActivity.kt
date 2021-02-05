package com.example.goorum

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.goorum.Utils.HttpHelper
import com.example.goorum.Utils.HttpMethod
import com.example.goorum.databinding.ActivitySignupBinding
import com.example.goorum.my.SignupFailureFragment
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    val TAG = "SignupActivity"
    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    lateinit var binding: ActivitySignupBinding
    lateinit var email: String
    lateinit var password: String
    lateinit var nickname: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        val etEmail = binding.etEmail.editText
        val etPassword = binding.etCreatePw.editText
        val etConfirm = binding.etConfirmPw.editText

        binding.ivClose.setOnClickListener {
            onBackPressed()
        }

        etEmail?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!isValidEmail(etEmail.text.toString())) {
                    etEmail.error = "유효하지 않은 이메일입니다."
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })

        etConfirm?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (etPassword?.text.toString() != etConfirm.text.toString()) {
                    etConfirm.error = "비밀번호가 일치하지 않습니다."
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })

        binding.bRegister.setOnClickListener {
            email = etEmail?.text.toString()
            password = etPassword?.text.toString()
            nickname = binding.etNickname.editText?.text.toString()
            Log.d(TAG, "email: $email / password: $password / nickname: $nickname")

            if (email.equals("") || password.equals("") || nickname.equals("")) {
                Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(email)) {
                Toast.makeText(this, "유효하지 않은 이메일입니다.", Toast.LENGTH_SHORT).show()
            } else if (etPassword?.text.toString() != etConfirm?.text.toString()) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else {
                onSubmit()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_down)
    }

    fun onSubmit() {
        Log.d(TAG, "회원가입 완료, email: $email / password: $password / nickname: $nickname")

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        notExist(email, password, nickname)

        startActivity(intent)
    }

    fun isValidEmail(str: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }

    fun notExist(e: String, p: String, n: String) {
        // 중복되는 이메일이 있는지 검사
        val url = "/member/check-duplicate"

        val data = JsonObject()
        data.addProperty("id", e)

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.GET, data)
            Log.d(TAG, result.toString())
            if (result["result"].asInt == 1) {
                register(e, p, n)
            } else {
                val dialog = SignupFailureFragment()
                dialog.show(supportFragmentManager, "signup failed")
            }
        }
    }

    fun register(e: String, p: String, n: String) {
        // 회원 등록
        val url = "/member/join"

        val data = JsonObject()
        data.addProperty("id", e)
        data.addProperty("pwd", p)
        data.addProperty("email", e)
        data.addProperty("nick", n)

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.POST, data)
            Log.d(TAG, result.toString())
            if (result["result"].asInt == 1) {
                onBackPressed()
            } else {
                Log.e(TAG, "Sign up failed")
            }

            return@launch
        }
    }
}