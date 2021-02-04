package com.example.goorum.data

import com.example.goorum.utils.HttpHelper
import com.example.goorum.utils.HttpMethod
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Signin(val email: String, val password: String) {
    fun matchesExistingAccount(): Boolean {
        var res = false
        val url = "/login"

        val data = JsonObject()
        data.addProperty("id", email)
        data.addProperty("pwd", password)

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.POST, data)
            if (result["result"].asInt == 1) {
                res = true
            }
        }

        return res
    }
}