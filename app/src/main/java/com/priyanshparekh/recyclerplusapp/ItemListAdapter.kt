package com.priyanshparekh.recyclerplusapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.priyanshparekh.recyclerplus.RecyclerPlusTouchHelper

class ItemListAdapter(val itemList: ArrayList<String>, val recyclerPlusTouchHelper: RecyclerPlusTouchHelper) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_view, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTextView.text = itemList[position]

        holder.itemTextView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(
                p0: View?
            ): Boolean {
                recyclerPlusTouchHelper.startDrag(holder)
                return true
            }

        })
    }

    override fun getItemCount(): Int = itemList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemTextView = view.findViewById<TextView>(R.id.tv_item)
    }
}