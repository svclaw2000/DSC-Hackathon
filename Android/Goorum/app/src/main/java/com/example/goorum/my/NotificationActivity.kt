package com.example.goorum.my

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goorum.ArticleActivity
import com.example.goorum.R
import com.example.goorum.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotificationBinding
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this,
             R.layout.activity_notification
         )

        val tvTitle = binding.tvTitle
        recyclerView = binding.lvNotification


        when (intent.extras?.getString("title")) {
            "my_activity" -> {
                tvTitle.text = "내 활동"

                val adapter =
                    LikesAdapter(this, MyArticle.myRet) { likesData ->
                        val intent = Intent(this, ArticleActivity::class.java)
                        intent.putExtra("articleId", likesData.id)
                        startActivity(intent)
                    }
                recyclerView.adapter = adapter
            }
            "likes" -> {
                tvTitle.text = "저장됨"

                val adapter =
                    LikesAdapter(this, MyArticle.likesRet) { likesData ->
                        val intent = Intent(this, ArticleActivity::class.java)
                        intent.putExtra("articleId", likesData.id)
                        startActivity(intent)
                    }
                recyclerView.adapter = adapter
            }
        }

        val lm = LinearLayoutManager(this)
        recyclerView.layoutManager = lm
        recyclerView.setHasFixedSize(false)

        binding.ivNotifiBack.setOnClickListener {
            onBackPressed()
        }
    }
}