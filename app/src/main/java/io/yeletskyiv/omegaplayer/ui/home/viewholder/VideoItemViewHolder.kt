package io.yeletskyiv.omegaplayer.ui.home.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import io.yeletskyiv.omegaplayer.R
import io.yeletskyiv.omegaplayer.databinding.VideoItemLayoutBinding
import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem

class VideoItemViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    inflater.inflate(R.layout.video_item_layout, parent, false)
) {

    private val viewBinding: VideoItemLayoutBinding by viewBinding(VideoItemLayoutBinding::bind)

    fun update(
        video: M3UVideoItem,
        onClick: (M3UVideoItem) -> Unit
    ) {
        itemView.setOnClickListener { onClick(video) }
        viewBinding.videoTitle.text = video.title
        viewBinding.videoImage.load(video.imageUrl) {
            crossfade(true)
        }
    }
}