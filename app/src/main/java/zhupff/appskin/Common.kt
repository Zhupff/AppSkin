package zhupff.appskin

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class CommonLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var cornerRadius: Float = 0F

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CommonLinearLayout).also {
            cornerRadius = it.getDimension(R.styleable.CommonLinearLayout_common_corner_radius, 0F)
        }.recycle()

        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                if (view == null || outline == null)
                    return
                outline.setRoundRect(0, 0, view.width, view.height, cornerRadius)
            }
        }
    }
}

class CommonConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var cornerRadius: Float = 0F

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CommonConstraintLayout).also {
            cornerRadius = it.getDimension(R.styleable.CommonConstraintLayout_common_corner_radius, 0F)
        }.recycle()

        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                if (view == null || outline == null)
                    return
                outline.setRoundRect(0, 0, view.width, view.height, cornerRadius)
            }
        }
    }
}

abstract class RecyclerViewAdapter<T, B : ViewDataBinding> :
    RecyclerView.Adapter<RecyclerViewHolder<B>>() {

    protected var data: List<T> = emptyList()

    protected var snapshot: List<Pair<String, String>> = emptyList()

    override fun getItemCount(): Int = snapshot.size

    abstract fun getSnapshotId(item: T): String

    abstract fun getSnapshotContent(item: T): String

    fun submit(newData: List<T>) {
        val newSnapshot = newData.map { getSnapshotId(it) to getSnapshotContent(it) }
        val differ = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = snapshot.size
            override fun getNewListSize(): Int = newSnapshot.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                snapshot[oldItemPosition].first == newSnapshot[newItemPosition].first
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                snapshot[oldItemPosition].second == newSnapshot[newItemPosition].second
        })
        data = newData
        snapshot = newSnapshot
        differ.dispatchUpdatesTo(this)
    }
}

open class RecyclerViewHolder<B : ViewDataBinding>(parent: ViewGroup, @LayoutRes id: Int) :
    RecyclerView.ViewHolder(
        DataBindingUtil.inflate<B>(
            LayoutInflater.from(parent.context),
            id,
            parent,
            false
        ).root
    ) {

    val binding: B = DataBindingUtil.getBinding(itemView)!!
}