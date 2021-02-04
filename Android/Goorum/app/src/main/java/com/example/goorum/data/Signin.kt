package com.example.goorum.data

import android.util.Log
import com.example.goorum.utils.HttpHelper
import com.example.goorum.utils.HttpMethod
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Signin(val email: String = "test", val password: String = "test") {
    val TAG = "Signin"

    fun matchesExistingAccount(): Boolean {
        var res = false
        val url = "/member/login"

        val data = JsonObject()
        data.addProperty("id", email)
        data.addProperty("pwd", password)

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.POST, data)
            Log.d(TAG, result.toString())
            if (result["result"].asInt == 1) {
                res = true
            }
        }

        return res
    }

    fun logout(): Boolean {
        var res = false
        val url = "/member/logout"

        val data = JsonObject()

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.GET, data)
            Log.d(TAG, result.toString())
            if (result["result"].asInt == 1) {
                res = true
            }
        }

        return res
    }
}