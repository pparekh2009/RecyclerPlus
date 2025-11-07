package com.priyanshparekh.recyclerplus

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.createBitmap

class SwipeConfig(builder: Builder) {

    internal var text: String? = builder.text
    internal var textSize: Float? = builder.textSize
    internal var textColor: Int? = builder.textColor
    internal var icon: Bitmap? = builder.icon
    internal var iconTint: Int? = builder.iconTint
    internal var iconSize: Int? = builder.iconSize
    internal var bgColor: Int? = builder.bgColor
    internal var onSwipeListener: ((Int) -> Unit)? = builder.onSwipeListener

    class Builder {

        internal var text: String? = null
        internal var textSize: Float? = null
        internal var textColor: Int? = null
        internal var icon: Bitmap? = null
        internal var iconTint: Int? = null
        internal var iconSize: Int? = null
        internal var bgColor: Int? = null
        internal var onSwipeListener: ((Int) -> Unit)? = null

        fun setText(text: String) = apply {
            this.text = text
        }

        fun setTextSize(textSize: Float) = apply {
            this.textSize = textSize
        }

        fun setTextColor(@ColorInt textColor: Int) = apply {
            this.textColor = textColor
        }

        fun setIcon(icon: Bitmap) = apply {
            this.icon = icon
        }

        fun setIcon(context: Context, @DrawableRes icon: Int)  = apply {
            val drawable = AppCompatResources.getDrawable(context, icon)

            val bitmap = createBitmap(48, 48)
            val canvas = Canvas(bitmap)

            drawable?.setBounds(0, 0, canvas.width, canvas.height)
            drawable?.draw(canvas)

            this.icon = bitmap
        }

        fun setIconSize(size: Int) = apply {
            this.iconSize = size
        }

        fun setIconTint(@ColorInt iconTint: Int) = apply {
            this.iconTint = iconTint
        }

        fun setBackgroundColor(@ColorInt color: Int) = apply {
            this.bgColor = color
        }

        fun setOnSwipeListener(onSwipeListener: ((Int) -> Unit)) = apply {
            this.onSwipeListener = onSwipeListener
        }

        fun build(): SwipeConfig = SwipeConfig(this)

    }

}