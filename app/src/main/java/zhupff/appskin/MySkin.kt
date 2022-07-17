package zhupff.appskin

import android.content.res.Resources

class MySkin(resources: Resources) : Skin(resources) {

    var isSelected: Boolean = false

    val name: String = info.getString("name")
}