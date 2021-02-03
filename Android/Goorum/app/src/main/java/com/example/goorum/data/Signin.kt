package com.example.goorum.data

class Signin {
    lateinit var email: String
    lateinit var password: String

    fun matchesExistingAccount(): Boolean {
        if (email == password) return true
        return false
    }
}