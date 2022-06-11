package io.github.teccheck.gear360app.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import io.github.teccheck.gear360app.R
import io.github.teccheck.gear360app.utils.ConnectionState


class ConnectionDots(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    init {
        val animated = AnimatedVectorDrawableCompat.create(context, R.drawable.ic_dots)
        animated?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                animated.start()
            }
        })
        setImageDrawable(animated)
    }

    fun setDotState(dotState: ConnectionState) {
        when (dotState) {
            ConnectionState.CONNECTING -> {
                imageTintList = null
                (drawable as AnimatedVectorDrawableCompat).start()
            }
            ConnectionState.CONNECTED -> {
                (drawable as AnimatedVectorDrawableCompat).stop()

                val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColor(R.color.primary, context.theme)
                } else {
                    resources.getColor(R.color.primary)
                }
                imageTintList = ColorStateList.valueOf(color)
            }
            ConnectionState.DISCONNECTED -> {
                (drawable as AnimatedVectorDrawableCompat).stop()
                imageTintList = null
            }
        }
    }
}