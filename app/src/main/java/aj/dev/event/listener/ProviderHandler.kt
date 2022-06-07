package aj.dev.event.listener

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface ProviderHandler {
    fun getString(@StringRes id: Int): String
    fun getColor(@ColorRes id: Int): Int
    fun getDrawable(@DrawableRes id: Int): Drawable?
}