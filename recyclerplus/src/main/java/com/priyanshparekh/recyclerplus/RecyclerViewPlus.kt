package com.priyanshparekh.recyclerplus

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewPlus @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): RecyclerView(context, attrs, defStyleAttr), DragCallback {

    private var touchHelper: RecyclerPlusTouchHelper? = null
    private lateinit var itemTouchHelper: ItemTouchHelper

    fun setTouchHelper(touchHelper: RecyclerPlusTouchHelper) {
        touchHelper.setDragCallback(this)

        this.touchHelper = touchHelper

        itemTouchHelper = ItemTouchHelper(touchHelper)
        itemTouchHelper.attachToRecyclerView(this)
    }

    override fun startDrag(viewHolder: ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

}