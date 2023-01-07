package io.yeletskyiv.omegaplayer.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem
import io.yeletskyiv.omegaplayer.model.entity.VideoCategory

data class CategoryWithVideos(
    @Embedded val category: VideoCategory,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "video_category_id"
    )
    val videos: List<M3UVideoItem>
)
