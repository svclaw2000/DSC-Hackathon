package com.example.goorum.my

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.goorum.App
import com.example.goorum.LoginActivity
import com.example.goorum.utils.HttpHelper
import com.example.goorum.utils.HttpMethod
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LogoutFragment : DialogFragment() {
    val TAG = "LogoutFragment"
    lateinit var mActivity: FragmentActivity

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mActivity = activity!!

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("로그아웃하시겠습니까?")
                .setPositiveButton("확인") { dialog, id ->
                    logout()
                }
                .setNegativeButton("닫기") {dialog, id ->

                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun logout() {
        val url = "/member/logout"
        val data = JsonObject()

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.GET, data)

            Log.d(TAG, result.toString())
            if (result["result"].asInt == 1) {
                val intent = Intent(mActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                App.prefs.userId = ""
                App.prefs.userPw = ""

                mActivity.startActivity(intent)
            } else {
                Toast.makeText(mActivity, "로그아웃에 실패하였습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }
}