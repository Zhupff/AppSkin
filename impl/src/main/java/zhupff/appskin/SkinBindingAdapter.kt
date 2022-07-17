package zhupff.appskin

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

object SkinBindingAdapter {

    @JvmStatic
    @BindingAdapter("skinTextColor")
    fun skinTextColor(view: View, @ColorRes id: Int) {
        SkinView.bind(view).run {
            addTask("skinTextColor") {
                SkinTask.skinTextColor(this, id)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("skinHintColor")
    fun skinHintColor(view: View, @ColorRes id: Int) {
        SkinView.bind(view).run {
            addTask("skinHintColor") {
                SkinTask.skinHintColor(this, id)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("skinForegroundDrawable")
    fun skinForegroundDrawable(view: View, @DrawableRes id: Int) {
        SkinView.bind(view).run {
            addTask("skinForegroundDrawable") {
                SkinTask.skinForegroundDrawable(this, id)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("skinBackgroundColor")
    fun skinBackgroundColor(view: View, @ColorRes id: Int) {
        SkinView.bind(view).run {
            addTask("skinBackgroundColor") {
                SkinTask.skinBackgroundColor(this, id)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("skinForegroundTint")
    fun skinForegroundTint(view: View, @ColorRes id: Int) {
        SkinView.bind(view).run {
            addTask("skinForegroundTint") {
                SkinTask.skinForegroundTint(this, id)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("skinBackgroundTint")
    fun skinBackgroundTint(view: View, @ColorRes id: Int) {
        SkinView.bind(view).run {
            addTask("skinBackgroundTint") {
                SkinTask.skinBackgroundTint(this, id)
            }
        }
    }
}