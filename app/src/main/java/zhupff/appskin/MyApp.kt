package zhupff.appskin

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MyApp : Application() {
    companion object {
        lateinit var DEFAULT_SKIN: MySkin; private set
        val SELECTED_SKIN: MutableLiveData<MySkin> = MutableLiveData()
        val ALL_SKINS: MutableLiveData<List<MySkin>> = MutableLiveData()

        @MainThread
        fun selectSkin(skin: MySkin) {
            var allSkins = ALL_SKINS.value ?: emptyList()
            if (!allSkins.contains(skin)) {
                allSkins = allSkins + skin
            }
            allSkins.forEach { it.isSelected = it == skin }
            ALL_SKINS.value = allSkins
            SELECTED_SKIN.value = skin
        }
    }

    override fun onCreate() {
        super.onCreate()
        DEFAULT_SKIN = MySkin(resources)
        selectSkin(DEFAULT_SKIN)
        loadSkinAssets()
    }

    private fun loadSkinAssets() {
        CoroutineScope(Dispatchers.IO).launch {
            val skins = ArrayList<MySkin>()
            resources.assets.list("")
                ?.filter { it.endsWith(".skin") }
                ?.forEach { assetName ->
                    val cacheFile = cacheDir.resolve(assetName)
                    saveFile(resources.assets.open(assetName), cacheFile)
                    DEFAULT_SKIN.loadSkinResources(cacheFile.path)?.let { res ->
                        skins.add(MySkin(res))
                    }
                }
            withContext(Dispatchers.Main) {
                val allSkins = (ALL_SKINS.value ?: emptyList()) + skins
                ALL_SKINS.value = allSkins
            }
        }
    }

    private fun saveFile(inputStream: InputStream, output: File) {
        FileOutputStream(output).use { outputStream ->
            val buffer = ByteArray(2048)
            inputStream.use { iStream ->
                outputStream.use { oStream ->
                    while (true) {
                        val length = iStream.read(buffer)
                        if (length != -1) {
                            oStream.write(buffer, 0, length)
                        } else {
                            oStream.flush()
                            break
                        }
                    }
                }
            }
        }
    }
}