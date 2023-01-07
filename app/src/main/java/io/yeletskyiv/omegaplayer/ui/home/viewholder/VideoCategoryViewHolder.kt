package io.yeletskyiv.omegaplayer.ui.home.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import io.yeletskyiv.omegaplayer.R
import io.yeletskyiv.omegaplayer.databinding.VideoCategoryItemLayoutBinding
import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem
import io.yeletskyiv.omegaplayer.model.relations.CategoryWithVideos
import io.yeletskyiv.omegaplayer.ui.home.adapter.VideosAdapter
import io.yeletskyiv.omegaplayer.utils.decorations.HorizontalListItemDecoration

class VideoCategoryViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    inflater.inflate(R.layout.video_category_item_layout, parent, false)
) {

    private val viewBinding: VideoCategoryItemLayoutBinding by viewBinding(VideoCategoryItemLayoutBinding::bind)

    fun update(
        categoryWithVideos: CategoryWithVideos,
        onClick: (M3UVideoItem) -> Unit
    ) {
        val videosAdapter = VideosAdapter(onClick)

        with(itemView) {
            viewBinding.categoryTitle.text = categoryWithVideos.category.name
            if (viewBinding.videosRecyclerView.itemDecorationCount > 0)
                viewBinding.videosRecyclerView.removeItemDecorationAt(0)
            viewBinding.videosRecyclerView.addItemDecoration(
                    HorizontalListItemDecoration(resources.getDimensionPixelSize(R.dimen.default_margin_mini))
                )
            viewBinding.videosRecyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            viewBinding.videosRecyclerView.adapter = videosAdapter
        }
        videosAdapter.submitList(categoryWithVideos.videos)
    }
}