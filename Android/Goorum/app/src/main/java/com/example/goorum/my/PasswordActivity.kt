package com.example.goorum.my

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.goorum.R
import com.example.goorum.databinding.ActivityPasswordBinding

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
            if (comparePassword()) {
                onBackPressed()
            } else {
                val dialog = PasswordChangeFailureFragment()
                dialog.show(supportFragmentManager, "login failed")
            }
        }

        binding.ivClose.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.stay, R.anim.slide_down)
    }

    fun comparePassword(): Boolean {
//        val signin = Signin()
//        if (signin.matchesCurPassword(etCurPassword.text.toString()))
//            return true
        return false
    }
}