package zhupff.appskin

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

object SkinTask {

    @JvmStatic
    fun skinTextColor(skinView: SkinView, @ColorRes id: Int) {
        val defaultSkin = skinView.context.getDefaultSkin()
        val targetSkin = skinView.context.getSelectedSkin().value ?: defaultSkin
        when (val view = skinView.view) {
            is TextView -> {
                view.setTextColor(targetSkin.getColorStateList(id, defaultSkin))
            }
        }
    }

    @JvmStatic
    fun skinHintColor(skinView: SkinView, @ColorRes id: Int) {
        val defaultSkin = skinView.context.getDefaultSkin()
        val targetSkin = skinView.context.getSelectedSkin().value ?: defaultSkin
        when (val view = skinView.view) {
            is EditText -> {
                view.setHintTextColor(targetSkin.getColorStateList(id, defaultSkin))
            }
        }
    }

    @JvmStatic
    fun skinForegroundDrawable(skinView: SkinView, @DrawableRes id: Int) {
        val defaultSkin = skinView.context.getDefaultSkin()
        val targetSkin = skinView.context.getSelectedSkin().value ?: defaultSkin
        when (val view = skinView.view) {
            is ImageView -> {
                view.setImageDrawable(targetSkin.getDrawable(id, defaultSkin))
            }
        }
    }

    @JvmStatic
    fun skinBackgroundColor(skinView: SkinView, @ColorRes id: Int) {
        val defaultSkin = skinView.context.getDefaultSkin()
        val targetSkin = skinView.context.getSelectedSkin().value ?: defaultSkin
        skinView.view.setBackgroundColor(targetSkin.getColor(id, defaultSkin))
    }

    @JvmStatic
    fun skinForegroundTint(skinView: SkinView, @ColorRes id: Int) {
        val defaultSkin = skinView.context.getDefaultSkin()
        val targetSkin = skinView.context.getSelectedSkin().value ?: defaultSkin
        when (val view = skinView.view) {
            is ImageView -> {
                view.setColorFilter(targetSkin.getColor(id, defaultSkin))
            }
        }
    }

    @JvmStatic
    fun skinBackgroundTint(skinView: SkinView, @ColorRes id: Int) {
        val defaultSkin = skinView.context.getDefaultSkin()
        val targetSkin = skinView.context.getSelectedSkin().value ?: defaultSkin
        skinView.view.backgroundTintList = targetSkin.getColorStateList(id, defaultSkin)
    }
}