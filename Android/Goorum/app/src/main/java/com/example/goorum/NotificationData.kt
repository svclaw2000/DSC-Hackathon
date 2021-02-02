package com.example.goorum

class NotificationData(category: String, content: String, time: String) {
    private val category: String = category
    private val content: String = content
    private val time: String = time

    fun getCategory(): String {
        return category
    }

    fun getContent(): String {
        return content
    }

    fun getTime(): String {
        return time
    }
}