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
import com.example.goorum.my.NotificationActivity
import com.example.goorum.my.MyActivity

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

        // TODO("추천글 RecyclerAdapter")

        val lCategory = Category.getAll()
        val adapter = BoardRecyclerAdapter(lCategory)
        val lm = LinearLayoutManager(this@MainActivity)
        rv_board.layoutManager = lm
        rv_board.adapter = adapter
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