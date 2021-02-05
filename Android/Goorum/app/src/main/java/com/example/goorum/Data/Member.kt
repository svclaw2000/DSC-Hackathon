package com.example.goorum.Data

import com.example.goorum.Utils.HttpHelper
import com.example.goorum.Utils.HttpMethod
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

public class Member(val id: Int, val username: String, val nickname: String, val email: String) {
    companion object {
        fun getFromJson(string: String) : Member {
            val json = JsonParser.parseString(string).asJsonObject
            return Member(
                id=json["id"].asInt,
                username=json["username"].asString,
                nickname=json["nickname"].asString,
                email=json["email"].asString
            )
        }

        fun getEmpty() : Member {
            return Member(
                id=-1,
                username="",
                nickname="",
                email=""
            )
        }

        fun getSample() : Member {
            return Member(
                id=1,
                username="testsample",
                nickname="샘플",
                email="test@sample.com"
            )
        }

        fun login() : Boolean {
            val data = JsonObject()
            data.addProperty("id", "test123")
            data.addProperty("pwd", "test123")

            var ret = false
            GlobalScope.launch(Dispatchers.Main) {
                val result = HttpHelper.request("/member/login", HttpMethod.POST, data)
                if (result["result"].asInt == 1) {
                    ret = true
                }
            }

            return ret
        }
    }

    fun toJsonString() : String {
        val json = JsonObject()
        json.addProperty("id", id)
        json.addProperty("username", username)
        json.addProperty("nickname", nickname)
        json.addProperty("email", email)
        return json.toString()
    }
}