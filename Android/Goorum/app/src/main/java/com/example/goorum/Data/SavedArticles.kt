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

class SavedArticles(
    val id: Int = -1, var title: String, var content: String, var date: Date,
    var likes: Int, var replies: Int, var like: Boolean, val writer: Member,
    var category: Category, var sector: String, var company: String, val replyArray: Array<Reply>) {

    companion object {
        fun getFromJson(string: String) : SavedArticles {
            val json = JsonParser.parseString(string).asJsonObject
            HttpHelper
            val replyJson = JsonParser.parseString(json["replyArray"].asString).asJsonArray
            val replyArray = Array(replyJson.size()) { Reply.getEmpty() }
            for (i in 0..replyJson.size()-1) {
                replyArray[i] = Reply.getFromJson(replyJson[i].asString)
            }

            return SavedArticles(
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

        suspend fun getById(id: Int) : SavedArticles {

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

            return SavedArticles(
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
                category = Category.getFromJson(result["category"].asString),
                sector = result["sector"].asString,
                company = result["company"].asString,
                replyArray = replyArray
            )
        }

        fun getEmpty() : SavedArticles {
            return SavedArticles(
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
    }
}