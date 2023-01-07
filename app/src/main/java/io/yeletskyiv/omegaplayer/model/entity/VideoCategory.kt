package io.yeletskyiv.omegaplayer.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_category")
data class VideoCategory(
    @PrimaryKey
    @ColumnInfo(name = "category_id") val categoryId: Int,
    val name: String
)
