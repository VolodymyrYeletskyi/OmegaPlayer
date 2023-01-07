package io.yeletskyiv.omegaplayer.repository

import com.haroldadmin.cnradapter.NetworkResponse
import io.yeletskyiv.omegaplayer.database.dao.M3UDao
import io.yeletskyiv.omegaplayer.mapper.mapM3ULinkContent
import io.yeletskyiv.omegaplayer.model.state.LinkScreenState
import io.yeletskyiv.omegaplayer.model.error.Error
import io.yeletskyiv.omegaplayer.model.state.HomeScreenState
import io.yeletskyiv.omegaplayer.network.M3ULinkApi

class M3URepository(
    private val m3ULinkApi: M3ULinkApi,
    private val m3UDao: M3UDao
) {

    suspend fun getM3UContent(url: String): LinkScreenState {
        return when (val result = m3ULinkApi.getM3UResponse(url)) {
            is NetworkResponse.Success -> {
                val mappedData = mapM3ULinkContent(result.body)
                mappedData.forEach {
                    m3UDao.insertM3UVideos(it)
                }
                LinkScreenState(
                    isLoading = false,
                    isSuccess = true,
                    error = null
                )
            }
            is NetworkResponse.NetworkError -> {
                LinkScreenState(
                    isLoading = false,
                    isSuccess = false,
                    error = Error.NetworkError
                )
            }
            is NetworkResponse.ServerError -> {
                LinkScreenState(
                    isLoading = false,
                    isSuccess = false,
                    error = Error.M3UDownloadError(result.error?.message)
                )
            }
            is NetworkResponse.UnknownError -> {
                LinkScreenState(
                    isLoading = false,
                    isSuccess = false,
                    error = Error.UnknownError(result.error.message)
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
                Error.DatabaseError
            )
        }
    }

    suspend fun clearM3UTables() {
        m3UDao.deleteAllCategories()
    }
}