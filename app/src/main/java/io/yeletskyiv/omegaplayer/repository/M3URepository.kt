package io.yeletskyiv.omegaplayer.repository

import com.haroldadmin.cnradapter.NetworkResponse
import io.yeletskyiv.omegaplayer.database.dao.M3UDao
import io.yeletskyiv.omegaplayer.mapper.mapM3ULinkContent
import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem
import io.yeletskyiv.omegaplayer.model.state.LinkScreenState
import io.yeletskyiv.omegaplayer.model.error.OmegaError
import io.yeletskyiv.omegaplayer.model.state.HomeScreenState
import io.yeletskyiv.omegaplayer.network.M3ULinkApi

class M3URepository(
    private val m3ULinkApi: M3ULinkApi,
    private val m3UDao: M3UDao
) {

    private var playedCategoryId = 0
    private var playedVideoId = 0

    suspend fun getM3UContent(url: String): LinkScreenState {
        return when (val result = m3ULinkApi.getM3UResponse(url)) {
            is NetworkResponse.Success -> {
                val mappedData = mapM3ULinkContent(result.body)
                try {
                    mappedData.forEach {
                        m3UDao.insertM3UVideos(it)
                    }
                    LinkScreenState(
                        isLoading = false,
                        isSuccess = true,
                        error = null
                    )
                } catch (exception: Exception) {
                    LinkScreenState(
                        isLoading = false,
                        isSuccess = false,
                        error = OmegaError.DatabaseOmegaError
                    )
                }
            }
            is NetworkResponse.NetworkError -> {
                LinkScreenState(
                    isLoading = false,
                    isSuccess = false,
                    error = OmegaError.NetworkOmegaError
                )
            }
            is NetworkResponse.ServerError -> {
                LinkScreenState(
                    isLoading = false,
                    isSuccess = false,
                    error = OmegaError.M3UDownloadOmegaError(result.error?.message)
                )
            }
            is NetworkResponse.UnknownError -> {
                LinkScreenState(
                    isLoading = false,
                    isSuccess = false,
                    error = OmegaError.UnknownOmegaError(result.error.message)
                )
            }
        }
    }

    suspend fun getVideos(): HomeScreenState {
        return try {
            val categories = m3UDao.getCategoriesWithVideos()
            HomeScreenState(
                false,
                categories,
                null
            )
        } catch (exception: Exception) {
            HomeScreenState(
                false,
                emptyList(),
                OmegaError.DatabaseOmegaError
            )
        }
    }

    fun setVideoCoordinates(categoryId: Int, startVideoId: Int) {
        playedCategoryId = categoryId
        playedVideoId = startVideoId
    }

    fun getStartVideoId() = playedVideoId

    suspend fun getVideosByCategory(): List<M3UVideoItem> {
        return try {
            m3UDao.getVideosByCategory(playedCategoryId)
        } catch (exception: Exception) {
            emptyList()
        }
    }

    suspend fun clearM3UTables() {
        m3UDao.deleteAllCategories()
    }
}