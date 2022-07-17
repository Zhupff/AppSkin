package zhupff.appskin

import androidx.lifecycle.LiveData

interface SkinContext<S : Skin> {

    fun getDefaultSkin(): S

    fun getSelectedSkin(): LiveData<S>
}