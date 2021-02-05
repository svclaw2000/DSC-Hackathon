package com.example.goorum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goorum.Data.Article
import com.example.goorum.Data.Reply
import com.example.goorum.Utils.SDF
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ArticleActivity : AppCompatActivity() {
    var articleId : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        btn_back.setOnClickListener {
            finish()
        }

        articleId = intent.getIntExtra("articleId", -1)
        refresh(articleId ?: return)

        btn_reply.setOnClickListener {
            if (et_reply.text.isBlank()) {
                Toast.makeText(this@ArticleActivity, "입력창이 비어있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            GlobalScope.launch(Dispatchers.Main) {
                if (Reply.write(articleId ?: return@launch, et_reply.text.toString())) {
                    et_reply.text.clear()
                    refresh(articleId ?: return@launch)
                }
            }
        }
    }

    fun refresh(id: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val article = Article.getById(id)

            tv_board_name.text = article.category.name
            tv_writer_name.text = article.writer.nickname
            tv_date.text = SDF.datetimeSlash.format(article.date)

            cb_like.isChecked = article.like
            cb_like.setOnCheckedChangeListener { btn, isChecked ->
                GlobalScope.launch(Dispatchers.Main) {
                    if (article.setLike(isChecked)) {
                        if (isChecked) {
                            article.likes += 1
                        } else {
                            article.likes -= 1
                        }
                        tv_like.text = article.likes.toString()
                    }
                }
            }

            val lReply = article.getReplies()

            tv_title.text = article.title
            tv_content.text = article.content
            tv_like.text = article.likes.toString()
            tv_reply.text = article.replies.toString()

            val adapter = ReplyRecyclerAdapter(lReply)
            val lm = LinearLayoutManager(this@ArticleActivity)
            rv_reply.layoutManager = lm
            rv_reply.adapter = adapter
        }
    }

    inner class ReplyRecyclerAdapter(val lReply : Array<Reply>) :
        RecyclerView.Adapter<ReplyRecyclerAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val writerName = itemView.findViewById<TextView>(R.id.tv_writer_name)
            val reply = itemView.findViewById<TextView>(R.id.tv_reply)
            val datetime = itemView.findViewById<TextView>(R.id.tv_date)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(this@ArticleActivity).inflate(R.layout.item_reply, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val reply = lReply[position]

            holder.writerName.text = reply.writer.nickname
            holder.reply.text = reply.content
            holder.datetime.text = SDF.datetimeSlash.format(reply.date)
        }

        override fun getItemCount(): Int {
            return lReply.size
        }
    }
}