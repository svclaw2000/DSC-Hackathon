package com.example.goorum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.goorum.Data.Article
import com.example.goorum.Data.Category
import kotlinx.android.synthetic.main.activity_write_article.*

class WriteArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_article)

        val json = intent.getStringExtra("category")
        val category = Category.getFromJson(json ?: return)

        btn_back.setOnClickListener {
            finish()
        }

        btn_save.setOnClickListener {
            val article = Article.getEmpty()
            article.title = et_title.text.toString()
            article.sector = et_title.text.toString()
            article.company = et_company.text.toString()
            article.category = category
            article.content = et_content.text.toString()

            if (article.save()) {
                finish()
            }
        }
    }
}