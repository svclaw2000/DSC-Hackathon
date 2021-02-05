package com.example.goorum.Data

import android.util.Log
import com.example.goorum.Utils.HttpHelper
import com.example.goorum.Utils.HttpMethod
import com.example.goorum.Utils.SDF
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SavedType(val id: Int, val name: String, val korean: String) {

    suspend fun getMyArticle() : Array<SavedArticles> {
        val data = JsonObject()

        val ret = withContext(Dispatchers.IO) {
            val result = HttpHelper.request("/mypage", HttpMethod.GET, data)

            val articles = result["board"].asJsonArray
            val ret = Array(articles.size()) { SavedArticles.getEmpty() }

            for (i in 0..articles.size()-1) {
                val article = articles[i].asJsonObject
                ret[i] = SavedArticles(
                    id = article["boardId"].asInt,
                    title = article["title"].asString,
                    content = article["content"].asString,
                    date = SDF.datetimeBar.parse(article["date"].asString),
                    likes =  article["likes"].asInt,
                    replies = article["replies"].asInt,
                    like = false,
                    writer = Member(
                        id = article["writerId"].asInt,
                        username = "",
                        nickname = article["writerNickname"].asString,
                        email = ""
                    ),
                    category = Category.findByKorean(article["category"].asString),
                    sector = article["sector"].asString,
                    company = article["company"].asString,
                    replyArray = arrayOf()
                )

                Log.d("내가 쓴글 제목", ret[i].title)
            }
            ret
        }

        return ret
    }
}