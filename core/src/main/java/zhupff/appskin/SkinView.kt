package zhupff.appskin

import android.content.ContextWrapper
import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.Observer
import zhupff.appskin.core.R

@MainThread
class SkinView(val view: View) {

    companion object {
        @JvmStatic
        @MainThread
        fun bind(view: View): SkinView {
            val instance = view.getTag(R.id.skin_view_tag)
            if (instance != null) {
                if (instance is SkinView)
                    return instance
                else
                    throw IllegalStateException("View($view) already bind an instance(${instance}).")
            }
            return SkinView(view)
        }

        @JvmStatic
        @MainThread
        fun unbind(view: View) {
            (view.getTag(R.id.skin_view_tag) as? SkinView)?.release()
        }
    }

    val context: SkinContext<*> = view.context.let {
        var c = it
        while (c is ContextWrapper) {
            if (c is SkinContext<*>)
                break
            c = c.baseContext
        }
        c as? SkinContext<*> ?: throw IllegalStateException("View($view)'s context isn't a SkinContext.")
    }

    private val tasks: HashMap<String, Runnable> = HashMap(4)

    private val onAttachStateChangeListener = object : View.OnAttachStateChangeListener {
        private var isAttached = false
        override fun onViewAttachedToWindow(v: View) {
            if (!isAttached) {
                isAttached = true
                context.getSelectedSkin().observeForever(onSkinChangedObserver)
            }
        }

        override fun onViewDetachedFromWindow(v: View) {
            if (isAttached) {
                isAttached = false
                context.getSelectedSkin().removeObserver(onSkinChangedObserver)
            }
        }
    }

    private val onSkinChangedObserver: Observer<Skin> = object : Observer<Skin> {
        private var currentSkinTimestamp = 0L
        override fun onChanged(skin: Skin?) {
            val newSkinTimestamp = skin?.timestamp ?: 0L
            if (newSkinTimestamp != currentSkinTimestamp) {
                currentSkinTimestamp = newSkinTimestamp
                tasks.values.forEach { it.run() }
            }
        }
    }

    init {
        view.getTag(R.id.skin_view_tag)?.let { instance ->
            throw IllegalStateException("View($view) already bind an instance(${instance}).")
        }
        view.setTag(R.id.skin_view_tag, this)
        view.addOnAttachStateChangeListener(onAttachStateChangeListener)
        onAttachStateChangeListener.onViewAttachedToWindow(view)
    }

    @MainThread
    fun release() {
        view.setTag(R.id.skin_view_tag, null)
        view.removeOnAttachStateChangeListener(onAttachStateChangeListener)
        onAttachStateChangeListener.onViewDetachedFromWindow(view)
    }

    @MainThread
    fun addTask(name: String, runWhenAdded: Boolean = true, task: Runnable) = apply {
        tasks[name] = task.also { if (runWhenAdded) it.run() }
    }

    @MainThread
    fun removeTask(name: String) = apply {
        tasks.remove(name)
    }
}