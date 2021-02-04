package com.example.goorum.like

class NotificationData(private val writer: String, private val title: String, private val category: String,
                       private val content: String, private val time: String) {
    fun getWriter(): String {
        return writer
    }

    fun getTitle(): String {
        return title
    }

    fun getCategory(): String {
        return category
    }

    fun getContent(): String {
        return content
    }

    fun getDate(): String {
        return time
    }
}