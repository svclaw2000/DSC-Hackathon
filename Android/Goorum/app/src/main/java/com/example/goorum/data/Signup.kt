package com.example.goorum.data

import org.json.JSONObject


class Signup() {
    lateinit var email: String
    lateinit var password: String
    lateinit var nickname: String

    fun exists(): Boolean {
       return true
    }

    fun register() {
    }
}


