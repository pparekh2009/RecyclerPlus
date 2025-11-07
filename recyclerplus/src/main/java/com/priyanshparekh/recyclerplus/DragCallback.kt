package com.priyanshparekh.recyclerplus

import androidx.recyclerview.widget.RecyclerView

interface DragCallback {

    fun startDrag(viewHolder: RecyclerView.ViewHolder)

}