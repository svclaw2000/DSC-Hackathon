package com.example.goorum.Data

import java.util.*

class Reply(val id: Int, val parent: Article, val content: String, val date: Date, val writer: Member) {
}