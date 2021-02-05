package com.example.goorum.Data

import com.example.goorum.Utils.HttpHelper
import com.example.goorum.Utils.HttpMethod
import com.example.goorum.Utils.SDF
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder

public class Category(val id: Int, val name: String, val korean: String) {
    companion object {
        val allCategory = arrayOf(
            Category(0, "전체 게시판", "전체"),
            Category(1, "추천 게시판", "추천"),
            Category(2, "비추천 게시판", "비추천"),
            Category(3, "신고/고발 게시판", "신고/고발"),
            Category(4, "자유 게시판", "자유")
        )


        fun getFromJson(string: String) : Category {
            val json = JsonParser.parseString(string).asJsonObject
            return Category(json["id"].asInt, json["name"].asString, json["korean"].asString)
        }

        fun getAll() : Array<Category> {
            return allCategory
        }

        fun getEmpty() : Category {
            return Category(
                id = -1,
                name = "",
                korean = ""
            )
        }

        fun findByKorean(korean: String) : Category {
            for (cat in allCategory) {
                if (cat.korean == korean) {
                    return cat
                }
            }
            return getEmpty()
        }
    }

    fun toJsonString() : String {
        val json = JsonObject()
        json.addProperty("id", id)
        json.addProperty("name", name)
        json.addProperty("korean", korean)
        return json.toString()
    }

    suspend fun getArticles(page: Int = 1, size: Int = 20, recommend: Boolean = false) : Array<Article> {
        if (HttpHelper.TEST_MODE) {
            return Article.getSamples(5, this)
        }

        val data = JsonObject()
        if (!recommend) {
            if (id != 0) {
                data.addProperty("category", URLEncoder.encode(korean, "UTF-8"))
            }
            data.addProperty("page", page)
        }
        data.addProperty("size", size)

        val ret = withContext(Dispatchers.IO) {
            val result = HttpHelper.request("/board", HttpMethod.GET, data)

            val articles = if (recommend) {
                result["topLikes"].asJsonArray
            } else {
                result["board"].asJsonArray
            }
            val ret = Array(articles.size()) { Article.getEmpty() }

            for (i in 0..articles.size()-1) {
                val article = articles[i].asJsonObject
                ret[i] = Article(
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
                    category = findByKorean(article["category"].asString),
                    sector = article["sector"].asString,
                    company = article["company"].asString,
                    replyArray = arrayOf()
                )
            }
            ret
        }

        return ret
    }
}