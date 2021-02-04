package com.example.goorum.my

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.goorum.App
import com.example.goorum.LoginActivity
import com.example.goorum.data.Signin


class LogoutFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("로그아웃하시겠습니까?")
                .setPositiveButton("확인") { dialog, id ->
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                    val signin = Signin()
                    if (signin.logout()) {
                        App.prefs.userId = ""
                        App.prefs.userPw = ""

                        startActivity(intent)
                    }
                }
                .setNegativeButton("닫기") {dialog, id ->

                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}