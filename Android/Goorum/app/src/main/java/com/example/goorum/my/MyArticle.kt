package com.example.goorum.my

import android.util.Log
import com.example.goorum.Data.Category
import com.example.goorum.Data.Member
import com.example.goorum.Data.SavedArticles
import com.example.goorum.Utils.SDF
import com.google.gson.JsonArray

class MyArticle() {
    companion object {
        lateinit var myArticle: JsonArray
        lateinit var likesArticle: JsonArray

        lateinit var myRet: Array<SavedArticles>
        lateinit var likesRet: Array<SavedArticles>

        fun parse(a: JsonArray, l: JsonArray) {
            myArticle = a
            likesArticle = l

            Log.d("myArticle", a.toString())

            myRet = Array(myArticle.size()) { SavedArticles.getEmpty() }
            likesRet = Array(likesArticle.size()) { SavedArticles.getEmpty() }

            Log.d("article size", myArticle.size().toString())

            for (i in 0..myArticle.size()-1) {
                val article = myArticle[i].asJsonObject
                myRet[i] = SavedArticles(
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

                Log.d("내가 쓴글 제목", myRet[i].title)
            }

            for (i in 0..likesArticle.size()-1) {
                val article = likesArticle[i].asJsonObject
                likesRet[i] = SavedArticles(
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

                Log.d("내가 좋아요한 글 제목", likesRet[i].title)
            }
        }
    }
}