package io.yeletskyiv.omegaplayer.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "video_item",
    foreignKeys = [
        ForeignKey(
            entity = VideoCategory::class,
            parentColumns = ["category_id"],
            childColumns = ["video_category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class M3UVideoItem(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "video_category_id") val videoCategoryId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "media_url") val mediaUrl: String?
)
