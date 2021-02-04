package com.example.goorum.data

import com.example.goorum.utils.HttpHelper
import com.example.goorum.utils.HttpMethod
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Signup(val email: String, val password: String, val nickname: String) {
    fun exists(): Boolean {
        // 중복되는 이메일이 있는지 검사
        var res = false
        val url = "/check-duplicate"

        val data = JsonObject()
        data.addProperty("id", "email")

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.POST, data)
            if (result["result"].asInt == 1) {
                res = true
            }
        }

       return res
    }

    fun register(): Boolean {
        // 회원 등록
        var res = false
        val url = "/join"

        val data = JsonObject()
        data.addProperty("id", email)
        data.addProperty("pwd", password)
        data.addProperty("email", email)
        data.addProperty("nick", nickname)

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.POST, data)
            if (result["result"].asInt == 1) {
                res = true
            }
        }

        return res
    }

    fun setNickname() {
        // 닉네임 설정
    }
}


