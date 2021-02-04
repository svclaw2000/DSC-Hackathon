package com.example.goorum.Data

import com.example.goorum.Utils.HttpHelper
import com.example.goorum.Utils.HttpMethod
import com.example.goorum.Utils.SDF
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class Article(
    val id: Int = -1, var title: String, var content: String, var date: Date,
    var likes: Int, var replies: Int, var like: Boolean, val writer: Member,
    var category: Category, var sector: String, var company: String, val replyArray: Array<Reply>
) {
    companion object {
        fun getFromJson(string: String) : Article {
            val json = JsonParser.parseString(string).asJsonObject

            val replyJson = JsonParser.parseString(json["replyArray"].asString).asJsonArray
            val replyArray = Array(replyJson.size()) { Reply.getEmpty() }
            for (i in 0..replyJson.size()-1) {
                replyArray[i] = Reply.getFromJson(replyJson[i].asString)
            }

            return Article(
                id = json["id"].asInt,
                title = json["title"].asString,
                content = json["content"].asString,
                date = SDF.datetimeSlash.parse(json["date"].asString),
                likes = json["likes"].asInt,
                replies = json["replies"].asInt,
                like = json["like"].asBoolean,
                writer = Member.getFromJson(json["writer"].asString),
                category = Category.getFromJson(json["category"].asString),
                sector = json["sector"].asString,
                company = json["company"].asString,
                replyArray = replyArray
            )
        }

        suspend fun getById(id: Int) : Article {
            if (HttpHelper.TEST_MODE) {
                return getSample()
            }

            val result = HttpHelper.request("/board/${id}", HttpMethod.GET)

            val replyJson = result["reply"].asJsonArray
            val replyArray = Array(replyJson.size()) { Reply.getEmpty() }
            for (i in 0..replyArray.size-1) {
                val reply = replyJson[i].asJsonObject
                replyArray[i] = Reply(
                    id = reply["replyId"].asInt,
                    content = reply["content"].asString,
                    date = SDF.datetimeBar.parse(reply["date"].asString),
                    writer = Member(
                        id = reply["memberId"].asInt,
                        username = "",
                        nickname = reply["nickname"].asString,
                        email = ""
                    )
                )
            }

            return Article(
                id = id,
                title = result["title"].asString,
                content = result["content"].asString,
                date = SDF.datetimeBar.parse(result["date"].asString),
                likes =  result["likes"].asInt,
                replies = result["replies"].asInt,
                like = result["isLike"].asBoolean,
                writer = Member(
                    id = result["writerId"].asInt,
                    username = "",
                    nickname = result["writerNickname"].asString,
                    email = ""
                ),
                category = Category.findByKorean(result["category"].asString),
                sector = result["sector"].asString,
                company = result["company"].asString,
                replyArray = replyArray
            )
        }

        fun getEmpty() : Article {
            return Article(
                id = -1,
                title = "",
                content = "",
                date = Date(),
                likes = 0,
                replies = 0,
                like = false,
                writer = Member.getEmpty(),
                category = Category.getEmpty(),
                sector = "",
                company = "",
                replyArray = arrayOf()
            )
        }

        fun getSample() : Article {
            return Article(
                id = 1,
                title = "테스트 제목",
                content = "테스트 콘텐츠입니다.",
                date = Date(),
                likes = 12,
                replies = 12,
                like = false,
                writer = Member.getSample(),
                category = Category.getAll()[0],
                sector = "개발",
                company = "구름",
                replyArray = Reply.getSamples(5, getEmpty())
            )
        }

        fun getSamples(n: Int, category: Category) : Array<Article> {
            val rand = Random()
            val array = Array(n) { getEmpty() }

            for (i in 1..n) {
                array[i-1] = Article(
                    id = i,
                    title = "테스트 글 ${i}",
                    content = "${i}번 테스트 콘텐츠입니다.",
                    date = Date(),
                    likes = rand.nextInt(50),
                    replies = rand.nextInt(50),
                    like = false,
                    writer = Member.getSample(),
                    category = category,
                    sector = "개발",
                    company = "구름",
                    replyArray = arrayOf()
                )
            }

            return array
        }
    }

    fun save() : Boolean {
        if (HttpHelper.TEST_MODE) {
            return true
        }

        val data = JsonObject()
        data.addProperty("title", title)
        data.addProperty("content", content)
        data.addProperty("category", category.id)
        data.addProperty("sector", sector)
        data.addProperty("company", company)

        var ret = false
        var url = "/board/write"

        if (id != -1) {
            data.addProperty("boardId", id)
            url = "/board/modify"
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
        if (HttpHelper.TEST_MODE) {
            return true
        }

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

    fun toJsonString() : String {
        val json = JsonObject()
        val replyJson = JsonArray()
        for (reply in replyArray) {
            replyJson.add(reply.toJsonString())
        }
        json.addProperty("id", id)
        json.addProperty("title", title)
        json.addProperty("content", content)
        json.addProperty("date", SDF.datetimeSlash.format(date))
        json.addProperty("likes", likes)
        json.addProperty("replies", replies)
        json.addProperty("like", like)
        json.addProperty("writer", writer.toJsonString())
        json.addProperty("category", category.toJsonString())
        json.addProperty("sector", sector)
        json.addProperty("company", company)
        json.addProperty("replyArray", replyJson.toString())
        return json.toString()
    }

    suspend fun setLike(flag: Boolean) : Boolean {
        if (HttpHelper.TEST_MODE) {
            return true
        }

        if (id == -1) {
            return false
        }

        val data = JsonObject()
        data.addProperty("boardId", id)
        data.addProperty("flag", if (flag) 1 else 0)

        val result = HttpHelper.request("/board/like", HttpMethod.POST, data)
        if (result["result"].asInt == 1) {
            return true
        }

        return false
    }

    fun getReplies() : Array<Reply> {
        return replyArray
    }
}