package com.priyanshparekh.recyclerplus

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.scale

class RecyclerPlusTouchHelper private constructor(builder: Builder): ItemTouchHelper.Callback() {

    private var isDragEnabled: Boolean = builder.isDragEnabled
    private var dragOnView: Boolean = builder.dragOnView

    private var rightSwipeConfig: SwipeConfig? = builder.rightSwipeConfig
    private var leftSwipeConfig: SwipeConfig? = builder.leftSwipeConfig

    private var isRightSwipeEnabled = rightSwipeConfig != null
    private var rightText: String = rightSwipeConfig?.text ?: ""
    private var rightTextSize: Float = rightSwipeConfig?.textSize ?: 40f
    private var rightTextColor: Int = rightSwipeConfig?.textColor ?: Color.BLACK
    private var rightIcon: Bitmap? = rightSwipeConfig?.icon
    private var rightIconTint: Int = rightSwipeConfig?.iconTint ?: Color.BLACK
    private var rightIconSize: Int = rightSwipeConfig?.iconSize ?: 48
    private var rightBackgroundColor: Int = rightSwipeConfig?.bgColor ?: Color.WHITE
    private var onRightSwipe: ((Int) -> Unit)? = rightSwipeConfig?.onSwipeListener

    private var isLeftSwipeEnabled = leftSwipeConfig != null
    private var leftText: String = leftSwipeConfig?.text ?: ""
    private var leftTextSize: Float = leftSwipeConfig?.textSize ?: 40f
    private var leftTextColor: Int = leftSwipeConfig?.textColor ?: Color.BLACK
    private var leftIcon: Bitmap? = leftSwipeConfig?.icon
    private var leftIconTint: Int = leftSwipeConfig?.iconTint ?: Color.BLACK
    private var leftIconSize: Int = leftSwipeConfig?.iconSize ?: 48
    private var leftBackgroundColor: Int = leftSwipeConfig?.bgColor ?: Color.WHITE
    private var onLeftSwipe: ((Int) -> Unit)? = leftSwipeConfig?.onSwipeListener


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {

        var movementFlags = when {
            isDragEnabled and !isRightSwipeEnabled and !isLeftSwipeEnabled -> makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
            isDragEnabled and isRightSwipeEnabled and !isLeftSwipeEnabled -> makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT)
            isDragEnabled and !isRightSwipeEnabled and isLeftSwipeEnabled -> makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT)
            isDragEnabled and isRightSwipeEnabled and isLeftSwipeEnabled -> makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT)
            !isDragEnabled and isRightSwipeEnabled and isLeftSwipeEnabled -> makeMovementFlags(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT)
            !isDragEnabled and !isRightSwipeEnabled and isLeftSwipeEnabled -> makeMovementFlags(0, ItemTouchHelper.LEFT)
            !isDragEnabled and isRightSwipeEnabled and !isLeftSwipeEnabled -> makeMovementFlags(0, ItemTouchHelper.RIGHT)
            else -> makeMovementFlags(0, 0)
        }

        return movementFlags
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (isDragEnabled) {

            val initialPosition = viewHolder.bindingAdapterPosition
            val targetPosition = target.bindingAdapterPosition

            recyclerView.adapter?.notifyItemMoved(initialPosition, targetPosition)

            return true
        }

        return false
    }

    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int
    ) {
        if (isRightSwipeEnabled && direction == ItemTouchHelper.RIGHT) {
            onRightSwipe?.invoke(viewHolder.bindingAdapterPosition)
            viewHolder.bindingAdapter?.notifyItemChanged(viewHolder.bindingAdapterPosition)
        }

        if (isLeftSwipeEnabled && direction == ItemTouchHelper.LEFT) {
            onLeftSwipe!!.invoke(viewHolder.bindingAdapterPosition)
            viewHolder.bindingAdapter?.notifyItemChanged(viewHolder.bindingAdapterPosition)
        }
    }

    override fun isLongPressDragEnabled(): Boolean = !dragOnView

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val paint = Paint()

        c.save()

        // Right Swipe
        if (dX > 0) {

            c.clipRect(
                itemView.left.toFloat(),
                itemView.top.toFloat(),
                itemView.left + dX,
                itemView.bottom.toFloat()
            )

            if (rightBackgroundColor != -1) {
                paint.color = rightBackgroundColor
                c.drawRect(
                    itemView.left.toFloat(),
                    itemView.top.toFloat(),
                    itemView.left + dX,
                    itemView.bottom.toFloat(),
                    paint
                )
            }

            val textExists = rightText.trim().isNotEmpty()
            val iconExists = rightIcon != null

            if (textExists and !iconExists) {
                paint.color = rightTextColor
                paint.textSize = rightTextSize
                paint.textAlign = Paint.Align.CENTER

                val fm = paint.fontMetrics
                val cy = itemView.top + itemView.height / 2f - (fm.ascent + fm.descent) / 2f
                val cx = itemView.left + dX / 2
                c.drawText(rightText, cx, cy, paint)
            }

            if (!textExists and iconExists) {
                if (rightIconSize != -1) {
                    rightIcon = rightIcon!!.scale(rightIconSize, rightIconSize)
                }

                val colorFilter = PorterDuffColorFilter(rightIconTint, PorterDuff.Mode.SRC_IN)
                paint.colorFilter = colorFilter

                val top = ((itemView.top + itemView.bottom) / 2 - (rightIcon!!.height / 2)).toFloat()
                val left = (itemView.left + (dX / 2) - (rightIcon!!.width / 2)).toFloat()

                c.drawBitmap(rightIcon!!, left, top, paint)
            }

            if (textExists and iconExists) {
                paint.color = rightTextColor
                paint.textSize = rightTextSize

                val fm = paint.fontMetrics
                val cy = itemView.top + itemView.height / 2f - (fm.ascent + fm.descent) / 2f
                val cx = itemView.left + (dX / 2) - ((paint.measureText(rightText) + rightIcon!!.width) / 2)
                c.drawText(rightText, cx, cy, paint)


                if (rightIconSize != -1) {
                    rightIcon = rightIcon!!.scale(rightIconSize, rightIconSize)
                }

                val colorFilter = PorterDuffColorFilter(rightIconTint, PorterDuff.Mode.SRC_IN)
                paint.colorFilter = colorFilter

                val left = cx + paint.measureText(rightText)
                val top = (itemView.top + (itemView.bottom - itemView.top) / 2) - (rightIcon!!.height / 2f)

                c.drawBitmap(rightIcon!!, left, top, paint)
            }

            c.restore()
        }
        // Left Swipe
        else if (dX < 0) {

            c.clipRect(
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )

            if (leftBackgroundColor != -1) {
                paint.color = leftBackgroundColor
                c.drawRect(
                    itemView.right + dX,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat(),
                    paint
                )
            }

            val textExists = leftText.trim().isNotEmpty()
            val iconExists = leftIcon != null

            if (textExists and !iconExists) {
                paint.color = leftTextColor
                paint.textSize = leftTextSize
                paint.textAlign = Paint.Align.CENTER

                val fm = paint.fontMetrics
                val cy = itemView.top + itemView.height / 2f - (fm.ascent + fm.descent) / 2f
                val cx = itemView.right + dX / 2
                c.drawText(leftText, cx, cy, paint)
            }

            if (!textExists and iconExists) {
                if (leftIconSize != -1) {
                    leftIcon = leftIcon!!.scale(leftIconSize, leftIconSize)
                }

                val colorFilter = PorterDuffColorFilter(leftIconTint, PorterDuff.Mode.SRC_IN)
                paint.colorFilter = colorFilter

                val left = (itemView.right + dX / 2) - (leftIcon!!.width / 2)
                val top = (itemView.top + (itemView.bottom - itemView.top) / 2) - (leftIcon!!.height / 2f)

                c.drawBitmap(leftIcon!!, left, top, paint)
            }

            if (textExists and iconExists) {
                // Draws Left Text
                paint.color = leftTextColor
                paint.textSize = leftTextSize

                val fm = paint.fontMetrics
                val cy = itemView.top + itemView.height / 2f - (fm.ascent + fm.descent) / 2f
                val cx = itemView.right + (dX / 2) - ((paint.measureText(leftText) + leftIcon!!.width) / 2)

                c.drawText(leftText, cx, cy, paint)



                // Draws Left Icon
                if (leftIconSize != -1) {
                    leftIcon = leftIcon!!.scale(leftIconSize, leftIconSize)
                }

                val colorFilter = PorterDuffColorFilter(leftIconTint, PorterDuff.Mode.SRC_IN)
                paint.colorFilter = colorFilter

                val left = cx + paint.measureText(leftText)
                val top = (itemView.top + (itemView.bottom - itemView.top) / 2) - (leftIcon!!.height / 2f)

                c.drawBitmap(leftIcon!!, left, top, paint)
            }

            c.restore()
        }

        getDefaultUIUtil().onDraw(
            c, recyclerView, itemView, dX, dY, actionState, isCurrentlyActive
        )
    }

    fun startDrag(viewHolder: RecyclerView.ViewHolder) {
        dragCallBack?.startDrag(viewHolder)
    }

    var dragCallBack: DragCallback? = null

    fun setDragCallback(dragCallback: DragCallback) {
        this.dragCallBack = dragCallback
    }

    class Builder() {

        internal var isDragEnabled = false
        internal var dragOnView: Boolean = false

        internal var leftSwipeConfig: SwipeConfig? = null
        internal var rightSwipeConfig: SwipeConfig? = null

        fun enableDrag() = apply {
            isDragEnabled = true
        }

        fun enableDragOnView() = apply {
            dragOnView = true
        }


        fun setLeftSwipeConfig(swipeConfig: SwipeConfig) = apply {
            this.leftSwipeConfig = swipeConfig
        }

        fun setRightSwipeConfig(swipeConfig: SwipeConfig) = apply {
            this.rightSwipeConfig = swipeConfig
        }

        fun create(): RecyclerPlusTouchHelper {
            return RecyclerPlusTouchHelper(this)
        }


    }
}