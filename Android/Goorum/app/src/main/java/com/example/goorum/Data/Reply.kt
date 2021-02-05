package com.example.goorum.Data

import com.example.goorum.Utils.HttpHelper
import com.example.goorum.Utils.HttpMethod
import com.example.goorum.Utils.SDF
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

class Reply(val id: Int, val content: String, val date: Date, val writer: Member) {
    companion object {
        fun getFromJson(string: String) : Reply {
            val json = JsonParser.parseString(string).asJsonObject
            return Reply(
                id = json["id"].asInt,
                content = json["content"].asString,
                date = SDF.datetimeSlash.parse(json["date"].asString),
                writer = Member.getFromJson(json["writer"].asString)
            )
        }

        fun getEmpty() : Reply {
            return Reply(
                id = -1,
                content = "",
                date = Date(),
                writer = Member.getEmpty()
            )
        }

        fun getSamples(n: Int, article: Article) : Array<Reply> {
            val array = Array(n) { getEmpty() }

            for (i in 1..n) {
                array[i-1] = Reply(
                    id = i,
                    content = "${i}번 테스트 댓글입니다",
                    date = Date(),
                    writer = Member.getSample()
                )
            }

            return array
        }

        suspend fun write(articleId: Int, content: String) : Boolean {
            val data = JsonObject()
            data.addProperty("boardId", articleId)
            data.addProperty("content", content)

            val result = HttpHelper.request("/board/write/reply", HttpMethod.POST, data)
            if (result["result"].asInt == 1) {
                return true
            }

            return false
        }
    }

    fun toJsonString() : String {
        val json = JsonObject()
        json.addProperty("id", id)
        json.addProperty("content", content)
        json.addProperty("date", SDF.datetimeSlash.format(date))
        json.addProperty("writer", writer.toJsonString())
        return json.toString()
    }
}