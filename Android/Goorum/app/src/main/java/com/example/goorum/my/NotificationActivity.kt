package com.example.goorum.my

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goorum.Data.SavedType
import com.example.goorum.R
import com.example.goorum.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotificationBinding
    lateinit var recyclerView: RecyclerView

    var savedType: SavedType? = null
    var likesList = arrayListOf<LikesData>()
    var type: Int? = null

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
                type = 1
//                getListData()

                val adapter =
                    LikesAdapter(this, MyArticle.myRet) { likesData ->
                        Toast.makeText(this, "${likesData.title} 본문으로 이동합니다.", Toast.LENGTH_SHORT).show()
                    }
                recyclerView.adapter = adapter
            }
            "likes" -> {
                tvTitle.text = "저장됨"
                type = 2

                val adapter =
                    LikesAdapter(this, MyArticle.likesRet) { likesData ->
                        Toast.makeText(this, "${likesData.title} 본문으로 이동합니다.", Toast.LENGTH_SHORT).show()
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

    fun getListData() {
        // TODO: 최신 100개
        for (i in 1..100) {
            likesList.add(
                LikesData(
                    "글쓴이", "제목 $i", "자유게시판",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur in porttitor eros. In eget nibh quam. Pellentesque in magna eros. Aliquam ac tempor ex, vel sollicitudin orci. Etiam posuere nibh eu quam placerat, et ullamcorper magna euismod. Nulla luctus leo vitae imperdiet efficitur. Donec ac libero ut turpis vehicula hendrerit.",
                    "02/02"
                )
            )
        }
    }
}