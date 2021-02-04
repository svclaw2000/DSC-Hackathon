package com.example.goorum.likes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goorum.R
import com.example.goorum.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotificationBinding
    var likesList = arrayListOf<LikesData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this,
             R.layout.activity_notification
         )

        val recyclerView = binding.lvNotification
        getListData()

        // TODO: RecyclerView로 변경
        val adapter = LikesAdapter(this, likesList) {likesData ->
            Toast.makeText(this, "${likesData.title} 본문으로 이동합니다.", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        val lm = LinearLayoutManager(this)
        recyclerView.layoutManager = lm
        recyclerView.setHasFixedSize(false)

//        binding.lvNotification.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
//            Toast.makeText(applicationContext,
//                adapter.getItem(i).getCategory(), Toast.LENGTH_LONG).show()
//        }

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