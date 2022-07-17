package zhupff.appskin

import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import zhupff.appskin.databinding.AlbumItemViewHolderBinding
import zhupff.appskin.databinding.MainActivityBinding
import zhupff.appskin.databinding.MusicItemViewHolderBinding
import zhupff.appskin.databinding.SkinItemViewHolderBinding
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity(), SkinContext<MySkin> {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        binding.ivSkin.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.sideBar)
        }

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val percent = verticalOffset.absoluteValue.toFloat() / appBarLayout.totalScrollRange.toFloat()
            binding.vSearchBg.alpha = percent
            binding.searchLayout.setPadding((binding.ivSkin.width * 1.5F * percent).toInt(), 0, 0, 0)
        })

        binding.rvAlbumList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvAlbumList.adapter = AlbumListAdapter()
        binding.rvAlbumList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildAdapterPosition(view)
                outRect.left = if (position <= 0) 48 else 0
                outRect.right = 48
            }
        })

        binding.rvMusicList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvMusicList.adapter = MusicListAdapter()

        binding.rvSkinList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvSkinList.adapter = SkinListAdapter()
        binding.rvSkinList.itemAnimator = null
        binding.rvSkinList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildAdapterPosition(view)
                outRect.top = if (position <= 0) 0 else 48
            }
        })

        MyApp.ALL_SKINS.observe(this) {
            (binding.rvSkinList.adapter as SkinListAdapter).submit(it ?: emptyList())
        }
    }

    override fun getDefaultSkin(): MySkin = MyApp.DEFAULT_SKIN

    override fun getSelectedSkin(): LiveData<MySkin> = MyApp.SELECTED_SKIN

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
            if (binding.drawerLayout.isDrawerOpen(binding.sideBar)) {
                binding.drawerLayout.closeDrawer(binding.sideBar)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    private class AlbumListAdapter : RecyclerViewAdapter<String, AlbumItemViewHolderBinding>() {
        init { submit(List(10) { it.toString() }) }
        override fun getSnapshotId(item: String): String = item
        override fun getSnapshotContent(item: String): String = item
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerViewHolder<AlbumItemViewHolderBinding> =
            RecyclerViewHolder(parent, R.layout.album_item_view_holder)
        override fun onBindViewHolder(
            holder: RecyclerViewHolder<AlbumItemViewHolderBinding>,
            position: Int
        ) { holder.binding.tvName.text = "Album ${position + 1}" }
    }


    private class MusicListAdapter : RecyclerViewAdapter<String, MusicItemViewHolderBinding>() {
        init {
            submit(List(1 + 20) { it.toString() })
        }
        override fun getSnapshotId(item: String): String = item
        override fun getSnapshotContent(item: String): String = item
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerViewHolder<MusicItemViewHolderBinding> =
            RecyclerViewHolder(parent, R.layout.music_item_view_holder)
        override fun onBindViewHolder(
            holder: RecyclerViewHolder<MusicItemViewHolderBinding>,
            position: Int
        ) {
            holder.binding.tvListTitle.isVisible = position == 0
            holder.binding.musicItemLayout.isVisible = position > 0
            holder.binding.tvNumber.text = position.toString()
        }
    }


    private class SkinListAdapter : RecyclerViewAdapter<MySkin, SkinItemViewHolderBinding>() {
        override fun getSnapshotId(item: MySkin): String = item.timestamp.toString()
        override fun getSnapshotContent(item: MySkin): String = "${item.timestamp}-${item.isSelected}"
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerViewHolder<SkinItemViewHolderBinding> =
            RecyclerViewHolder(parent, R.layout.skin_item_view_holder)
        override fun onBindViewHolder(
            holder: RecyclerViewHolder<SkinItemViewHolderBinding>,
            position: Int
        ) {
            val skin = data[position]
            holder.binding.skin = skin
            holder.binding.ivCover.setImageDrawable(skin.getDrawable(R.drawable.akali, MyApp.DEFAULT_SKIN))
            holder.binding.ivCheck.setOnClickListener {
                MyApp.selectSkin(skin)
            }
        }
    }
}