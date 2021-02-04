package com.example.goorum.my

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class PasswordChangeFailureFragment : DialogFragment() {
    override fun onCreateDialog(args: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("현재 비밀번호가 올바르지 않습니다.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}