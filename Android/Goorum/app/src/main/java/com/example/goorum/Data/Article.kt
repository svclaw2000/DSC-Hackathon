package com.example.goorum.Data

import com.example.goorum.Utils.HttpHelper
import com.example.goorum.Utils.HttpMethod
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class Article(
    val id: Int = -1, var title: String, var content: String, var date: Date, var likes: Int, var views: Int,
    val writer: Member, var category: Category, var sector: String, var company: String
) {
    fun save() : Boolean {
        val data = JsonObject()
        data.addProperty("title", title)
        data.addProperty("content", content)
        data.addProperty("category", category.name)
        data.addProperty("sector", sector)
        data.addProperty("company", company)

        var ret = false
        var url = "/write"

        if (id != -1) {
            data.addProperty("boardId", id)
            url = "/modify"
        }

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.POST, data)
            if (result["result"].asInt == 1) {
                ret = true
            }
        }

        return ret
    }

    fun delete() : Boolean {
        if (id == -1) {
            return false
        }

        var ret = false

        val data = JsonObject()
        data.addProperty("boardId", id)

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request("/delete", HttpMethod.POST, data)
            if (result["result"].asInt == 1) {
                ret = true
            }
        }

        return ret
    }
}