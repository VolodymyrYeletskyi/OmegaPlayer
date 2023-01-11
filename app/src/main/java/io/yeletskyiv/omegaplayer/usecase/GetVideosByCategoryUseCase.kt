package io.yeletskyiv.omegaplayer.usecase

import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem
import io.yeletskyiv.omegaplayer.repository.M3URepository

class GetVideosByCategoryUseCase(
    private val m3URepository: M3URepository
) {

    suspend operator fun invoke(): Pair<List<M3UVideoItem>, Int> {
        val result = m3URepository.getVideosByCategory()
        val startVideo = result.find { it.id == m3URepository.getStartVideoId() }
        val startVideoIndex = startVideo?.let { result.indexOf(startVideo) } ?: 0
        return Pair(result, startVideoIndex)
    }
}