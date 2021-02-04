package com.example.goorum.like

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.goorum.R
import com.example.goorum.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotificationBinding
    lateinit var listData: ArrayList<NotificationData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this,
             R.layout.activity_notification
         )

        getListData()

        // TODO: RecyclerView로 변경
        val adapter = NotificationAdapter(this, listData)
        binding.lvNotification.adapter = adapter

        binding.lvNotification.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            Toast.makeText(applicationContext,
                adapter.getItem(i).getCategory(), Toast.LENGTH_LONG).show()
        }

        binding.ivNotifiBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun getListData() {
        listData = ArrayList()

        // TODO: 최신 100개
        for (i in 1..100) {
            listData.add(
                NotificationData(
                    "글쓴이", "제목 $i", "자유게시판",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur in porttitor eros. In eget nibh quam. Pellentesque in magna eros. Aliquam ac tempor ex, vel sollicitudin orci. Etiam posuere nibh eu quam placerat, et ullamcorper magna euismod. Nulla luctus leo vitae imperdiet efficitur. Donec ac libero ut turpis vehicula hendrerit.",
                    "02/02"
                )
            )
        }
    }
}