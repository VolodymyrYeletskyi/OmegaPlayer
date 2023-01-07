package io.yeletskyiv.omegaplayer.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem
import io.yeletskyiv.omegaplayer.model.relations.CategoryWithVideos
import io.yeletskyiv.omegaplayer.ui.home.adapter.diffutil.CategoryWithVideosDiffUtil
import io.yeletskyiv.omegaplayer.ui.home.viewholder.VideoCategoryViewHolder

class CategoryWithVideosAdapter(
    private val onClick: (M3UVideoItem) -> Unit
) : ListAdapter<CategoryWithVideos, VideoCategoryViewHolder>(CategoryWithVideosDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VideoCategoryViewHolder(LayoutInflater.from(parent.context), parent)

    override fun onBindViewHolder(holder: VideoCategoryViewHolder, position: Int) {
        holder.update(getItem(position), onClick)
    }
}