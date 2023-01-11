package io.yeletskyiv.omegaplayer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.yeletskyiv.omegaplayer.database.dao.M3UDao
import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem
import io.yeletskyiv.omegaplayer.model.entity.VideoCategory

@Database(
    entities = [
        VideoCategory::class,
        M3UVideoItem::class
    ],
    exportSchema = true,
    version = 3
)
abstract class OmegaDatabase : RoomDatabase() {

    abstract fun m3uCategoryDao(): M3UDao
}