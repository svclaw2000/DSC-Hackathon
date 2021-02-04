package com.example.goorum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goorum.Data.Article
import com.example.goorum.Data.Category
import com.example.goorum.Utils.SDF
import kotlinx.android.synthetic.main.activity_board.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BoardActivity : AppCompatActivity() {
    var category : Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        btn_back.setOnClickListener {
            finish()
        }

        val json = intent.getStringExtra("category")
        category = Category.getFromJson(json ?: return)

        tv_board_name.text = category?.name

        btn_write.setOnClickListener {
            val intent = Intent(this@BoardActivity, WriteArticleActivity::class.java)
            intent.putExtra("category", category?.toJsonString())
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    fun refresh() {
        GlobalScope.launch(Dispatchers.Main) {
            val lArticle = category?.getArticles() ?: return@launch
            Log.d("@@@", lArticle.size.toString())
            val adapter = ArticleRecyclerAdapter(lArticle)
            val lm = LinearLayoutManager(this@BoardActivity)
            rv_board.layoutManager = lm
            rv_board.adapter = adapter
        }
    }

    inner class ArticleRecyclerAdapter(val lArticle : Array<Article>) :
        RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val container = itemView.findViewById<LinearLayout>(R.id.container)
            val writerName = itemView.findViewById<TextView>(R.id.tv_writer_name)
            val datetime = itemView.findViewById<TextView>(R.id.tv_date)
            val title = itemView.findViewById<TextView>(R.id.tv_title)
            val content = itemView.findViewById<TextView>(R.id.tv_content)
            val board = itemView.findViewById<TextView>(R.id.tv_board)
            val like = itemView.findViewById<TextView>(R.id.tv_like)
            val reply = itemView.findViewById<TextView>(R.id.tv_reply)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@BoardActivity).inflate(R.layout.item_article, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val article = lArticle[position]
            holder.container.setOnClickListener {
                val intent = Intent(this@BoardActivity, ArticleActivity::class.java)
                intent.putExtra("articleId", article.id)
                startActivity(intent)
            }

            holder.writerName.text = article.writer.nickname
            holder.datetime.text = SDF.datetimeSlash.format(article.date)
            holder.title.text = article.title
            holder.content.text = article.content
            holder.board.text = article.category.name
            holder.like.text = article.likes.toString()
            holder.reply.text = article.replies.toString()
        }

        override fun getItemCount(): Int {
            return lArticle.size
        }
    }
}