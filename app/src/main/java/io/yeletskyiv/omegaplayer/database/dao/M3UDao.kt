package io.yeletskyiv.omegaplayer.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem
import io.yeletskyiv.omegaplayer.model.entity.VideoCategory
import io.yeletskyiv.omegaplayer.model.relations.CategoryWithVideos

@Dao
interface M3UDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(category: VideoCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<M3UVideoItem>)

    @Query("SELECT * FROM video_item WHERE video_category_id LIKE :categoryId")
    suspend fun getVideosByCategory(categoryId: Int): List<M3UVideoItem>

    @Transaction
    @Query("SELECT * FROM video_category")
    suspend fun getCategoriesWithVideos(): List<CategoryWithVideos>

    @Transaction
    suspend fun insertM3UVideos(categoryWithVideos: CategoryWithVideos) {
        insertCategories(categoryWithVideos.category)
        insertVideos(categoryWithVideos.videos)
    }

    @Query("DELETE FROM video_category")
    suspend fun deleteAllCategories()
}