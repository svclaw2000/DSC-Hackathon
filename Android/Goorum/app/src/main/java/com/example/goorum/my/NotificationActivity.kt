package com.example.goorum.my

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goorum.R
import com.example.goorum.databinding.ActivityNotificationBinding
import com.example.goorum.utils.HttpHelper
import com.example.goorum.utils.HttpMethod
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotificationBinding
    var likesList = arrayListOf<LikesData>()
    var type: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this,
             R.layout.activity_notification
         )

        val tvTitle = binding.tvTitle

        when (intent.extras?.getString("title")) {
            "my_activity" -> {
                tvTitle.text = "내 활동"
                type = 1
            }
            "likes" -> {
                tvTitle.text = "저장됨"
                type = 2
            }
        }

        val recyclerView = binding.lvNotification
        getListData(type)

        val adapter =
            LikesAdapter(this, likesList) { likesData ->
                Toast.makeText(this, "${likesData.title} 본문으로 이동합니다.", Toast.LENGTH_SHORT).show()

                // TODO: 게시글 개별 조회 페이지에 boardId 전달
                // LikesData에 boardId 추가
            }
        recyclerView.adapter = adapter

        val lm = LinearLayoutManager(this)
        recyclerView.layoutManager = lm
        recyclerView.setHasFixedSize(false)

        binding.ivNotifiBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun getListData(t: Int?) {
        val url = "/mypage"
        val data = JsonObject()

        GlobalScope.launch(Dispatchers.Main) {
            val result = HttpHelper.request(url, HttpMethod.GET, data)

            if (t == 1) { // 내가 쓴 글
                for (i in 1..100) {
                    likesList.add(
                        LikesData(
                            "글쓴이", "내가 쓴 게시글 $i", "자유게시판",
                            "최\n대\n다\n섯\n줄",
                            "02/02"
                        )
                    )
                }

                val ids = result["board"]
                // TODO: parsing, 게시물 기본 조회
            }
            else { // 좋아요 누른 글
                for (i in 1..100) {
                    likesList.add(
                        LikesData(
                            "글쓴이", "좋아요 누른 게시글 $i", "자유게시판",
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur in porttitor eros. In eget nibh quam. Pellentesque in magna eros. Aliquam ac tempor ex, vel sollicitudin orci. Etiam posuere nibh eu quam placerat, et ullamcorper magna euismod. Nulla luctus leo vitae imperdiet efficitur. Donec ac libero ut turpis vehicula hendrerit.",
                            "02/02"
                        )
                    )
                }

                val ids = result["likeboard"]
                // TODO: parsing, 게시물 기본 조회
            }
        }
    }
}