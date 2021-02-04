package com.example.goorum.data

class Signin {
    lateinit var email: String
    lateinit var password: String

    fun matchesExistingAccount(): Boolean {
        if (email == password) return true
        return false
    }

    fun matchesCurPassword(input: String): Boolean {
        password = "00"
        if (password == input) return true
        return false
    }
}