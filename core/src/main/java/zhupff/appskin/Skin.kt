package zhupff.appskin

import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import org.json.JSONObject

open class Skin(val resources: Resources) {

    val info: JSONObject = try {
        JSONObject(resources.assets.open("skin.json").reader().readText())
    } catch (e: Exception) {
        JSONObject()
    }

    val applicationId: String = info.getString("applicationId")

    val cache: HashMap<Int, Int> = HashMap()

    val timestamp: Long = System.currentTimeMillis()


    open fun loadSkinResources(skinFilePath: String): Resources? {
        return try {
            val assetManager = AssetManager::class.java.newInstance()
            val addAssetPath = AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPath.invoke(assetManager, skinFilePath)
            Resources(assetManager, resources.displayMetrics, resources.configuration)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    open fun getIdentifier(@AnyRes id: Int, defaultSkin: Skin): Int {
        if (this === defaultSkin || id == ResourcesCompat.ID_NULL)
            return id
        return cache.getOrPut(id) {
            try {
                val name = defaultSkin.resources.getResourceEntryName(id)
                val type = defaultSkin.resources.getResourceTypeName(id)
                resources.getIdentifier(name, type, applicationId)
            } catch (e: Exception) {
                e.printStackTrace()
                ResourcesCompat.ID_NULL
            }
        }
    }

    @ColorInt
    open fun getColor(@ColorRes id: Int, defaultSkin: Skin): Int {
        val realId = getIdentifier(id, defaultSkin)
        return if (realId != ResourcesCompat.ID_NULL) {
            ResourcesCompat.getColor(resources, realId, null)
        } else {
            ResourcesCompat.getColor(defaultSkin.resources, id, null)
        }
    }

    open fun getColorStateList(@ColorRes id: Int, defaultSkin: Skin): ColorStateList {
        val realId = getIdentifier(id, defaultSkin)
        return if (realId != ResourcesCompat.ID_NULL) {
            ResourcesCompat.getColorStateList(resources, realId, null)
        } else {
            ResourcesCompat.getColorStateList(defaultSkin.resources, id, null)
        } ?: ColorStateList.valueOf(getColor(id, defaultSkin))
    }

    open fun getDrawable(@DrawableRes id: Int, defaultSkin: Skin): Drawable? {
        val realId = getIdentifier(id, defaultSkin)
        return if (realId != ResourcesCompat.ID_NULL) {
            ResourcesCompat.getDrawable(resources, realId, null)
        } else {
            ResourcesCompat.getDrawable(defaultSkin.resources, id, null)
        }
    }

    open fun getString(@StringRes id: Int, defaultSkin: Skin): String {
        val realId = getIdentifier(id, defaultSkin)
        return if (realId != ResourcesCompat.ID_NULL) {
            resources.getString(realId)
        } else {
            defaultSkin.resources.getString(id)
        }
    }

    override fun toString(): String = info.toString()
}