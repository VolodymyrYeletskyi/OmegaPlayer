package io.yeletskyiv.omegaplayer.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem
import io.yeletskyiv.omegaplayer.ui.home.adapter.diffutil.VideosDiffUtil
import io.yeletskyiv.omegaplayer.ui.home.viewholder.VideoItemViewHolder

class VideosAdapter(private val onClick: (M3UVideoItem) -> Unit) :
    ListAdapter<M3UVideoItem, VideoItemViewHolder>(VideosDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VideoItemViewHolder(LayoutInflater.from(parent.context), parent)

    override fun onBindViewHolder(holder: VideoItemViewHolder, position: Int) {
        holder.update(getItem(position), onClick)
    }
}