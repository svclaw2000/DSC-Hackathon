package com.example.goorum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goorum.Data.Category
import kotlinx.android.synthetic.main.activity_main.*
import android.view.Menu
import android.view.MenuItem
import com.example.goorum.Data.Article
import com.example.goorum.Utils.SDF
import com.example.goorum.my.NotificationActivity
import com.example.goorum.my.MyActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_search.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        btn_my.setOnClickListener {
            val intent = Intent(this@MainActivity, MyActivity::class.java)
            startActivity(intent)
        }

        val lCategory = Category.getAll()
        val adapter = BoardRecyclerAdapter(lCategory)
        val lm = LinearLayoutManager(this@MainActivity)
        rv_board.layoutManager = lm
        rv_board.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        refresh()
    }

    fun refresh() {
        GlobalScope.launch(Dispatchers.Main) {
            val lArticles = Category.allCategory[1].getArticles(size=5, recommend=true)
            val adapter = ArticleRecyclerAdapter(lArticles)
            val lm = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            rv_recommend.layoutManager = lm
            rv_recommend.adapter = adapter
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
            val sector = itemView.findViewById<TextView>(R.id.tv_sector)
            val company = itemView.findViewById<TextView>(R.id.tv_company)
            val like = itemView.findViewById<TextView>(R.id.tv_like)
            val reply = itemView.findViewById<TextView>(R.id.tv_reply)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.item_article, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val article = lArticle[position]
            holder.container.setOnClickListener {
                val intent = Intent(this@MainActivity, ArticleActivity::class.java)
                intent.putExtra("articleId", article.id)
                startActivity(intent)
            }

            holder.writerName.text = article.writer.nickname
            holder.datetime.text = SDF.datetimeSlash.format(article.date)
            holder.title.text = article.title
            holder.content.text = article.content
            holder.board.text = article.category.name
            holder.sector.text = article.sector
            holder.company.text = article.company
            holder.like.text = article.likes.toString()
            holder.reply.text = article.replies.toString()
        }

        override fun getItemCount(): Int {
            return lArticle.size
        }
    }

    inner class BoardRecyclerAdapter(val lCategory : Array<Category>) :
        RecyclerView.Adapter<BoardRecyclerAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val container = itemView.findViewById<LinearLayout>(R.id.container)
            val boardName = itemView.findViewById<TextView>(R.id.tv_board_name)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.item_main_board, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val category = lCategory[position]
            holder.boardName.text = category.name
            holder.container.setOnClickListener {
                val intent = Intent(this@MainActivity, BoardActivity::class.java)
                intent.putExtra("category", category.toJsonString())
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return lCategory.size
        }
    }
}