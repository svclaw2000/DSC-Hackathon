package com.example.goorum

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class NotificationAdapter(context: Context?, data: ArrayList<NotificationData>?) : BaseAdapter() {
    var mContext: Context? = context
    var mLayoutInflater: LayoutInflater? = null
    var item: ArrayList<NotificationData>? = data

    init {
        mLayoutInflater = LayoutInflater.from(mContext)
    }

    override fun getView(position: Int, covertView: View?, parent: ViewGroup?): View {
        val view = mLayoutInflater!!.inflate(R.layout.notification_list_item, null)

        val category = view.findViewById<TextView>(R.id.tvCategoryInfo)
        val content = view.findViewById<TextView>(R.id.tvContentPreview)
        val time = view.findViewById<TextView>(R.id.tvTimeInfo)

        category.text = item!![position].getCategory()
        content.text = item!![position].getContent()
        time.text = item!![position].getTime()

        return view
    }

    override fun getItem(position: Int): NotificationData {
        return item!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return item!!.size
    }
}