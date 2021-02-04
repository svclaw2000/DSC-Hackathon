package com.example.goorum.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.example.goorum.R
import com.example.goorum.data.Signup
import com.example.goorum.databinding.ActivityNicknameBinding

class NicknameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNicknameBinding

    lateinit var etNickname: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_nickname
        )

        etNickname = binding.etNickname.editText!!

        etNickname.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (etNickname.text.toString().length < 2) {
                    etNickname.error = "2자 이상 입력하세요"
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })

        binding.ivClose.setOnClickListener {
            onBackPressed()
        }
        binding.bSetNickname.setOnClickListener {
            onSetNickname()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.stay,
            R.anim.slide_down
        )
    }

    private fun onSetNickname() {
        // TODO: 서버에 전달
        val strNickname = etNickname.text.toString()
//        val signup = Signup()

//        signup.nickname = strNickname
//        signup.setNickname()

        onBackPressed()
    }
}