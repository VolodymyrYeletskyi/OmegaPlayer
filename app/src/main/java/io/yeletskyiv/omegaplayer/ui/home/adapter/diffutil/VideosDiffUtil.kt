package io.yeletskyiv.omegaplayer.ui.home.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem

class VideosDiffUtil : DiffUtil.ItemCallback<M3UVideoItem>() {

    override fun areItemsTheSame(oldItem: M3UVideoItem, newItem: M3UVideoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: M3UVideoItem, newItem: M3UVideoItem): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.videoCategoryId == newItem.videoCategoryId &&
                oldItem.title == newItem.title &&
                oldItem.imageUrl == newItem.imageUrl &&
                oldItem.mediaUrl == newItem.mediaUrl
    }
}