package com.example.goorum.data


class Signup() {
    lateinit var email: String
    lateinit var password: String
    lateinit var nickname: String

    fun exists(): Boolean {
        // 중복되는 이메일이 있는지 검사
       return true
    }

    fun setInformations() {
        // 회원 등록
    }

    fun setNickname() {
        // 닉네임 설정
    }
}


